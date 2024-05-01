package si.feri.itk.projectmanager.cron;

import io.github.zzhorizonzz.client.models.EmailAddress;
import io.github.zzhorizonzz.client.models.User;
import io.github.zzhorizonzz.client.users.item.WithUserPatchRequestBody;
import io.github.zzhorizonzz.sdk.ClerkClient;
import io.github.zzhorizonzz.sdk.user.UserService;
import io.github.zzhorizonzz.sdk.user.request.ListAllUsersRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import si.feri.itk.projectmanager.model.Person;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.util.StringUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class ClerkUserSync {
    private final ClerkClient clerkClient;
    private final PersonRepo personRepo;
    private final boolean syncLocal;

    public ClerkUserSync(ClerkClient clerkClient, PersonRepo personRepo, @Value("${clerk.sync-local}") String syncLocal) {
        this.clerkClient = clerkClient;
        this.personRepo = personRepo;
        this.syncLocal = syncLocal.equals("true");
    }

    @EventListener(ApplicationReadyEvent.class)
    private void syncUsersWithClerk() {
        ListAllUsersRequest request = new ListAllUsersRequest();
        UserService userService = clerkClient.getUserService();
        request.setOrderBy("-created_at");

        final Long total = userService.count(request).getTotalCount();
        if (total == null || total == 0) {
            return;
        }

        final int limit = 500;//max limit is 500
        request.setLimit(limit);

        int offset = 0;
        while (offset < total) {
            request.setOffset(offset);
            List<User> users = userService.listAll(request);
            processUsers(users);
            offset += limit;
        }
    }

    private void processUsers(List<User> users) {
        UserService userService = clerkClient.getUserService();
        for (User user : users) {
            if (!StringUtil.isNullOrEmpty(user.getExternalId())) {
                //we don't update our persons with external data
                //we could probably skip users after we find one with external id because we order by created_at
                if (syncLocal) {
                    try {
                        Optional<Person> person = personRepo.findById(UUID.fromString(user.getExternalId()));
                        if (person.isEmpty()) {
                            Person newPerson = createPersonFromUser(user);
                            personRepo.save(newPerson);
                        }
                    } catch (Exception e) {
                        //this should only happen in dev and test environment, if at all
                        log.error("Failed to find person with id: " + user.getExternalId());
                    }
                }
                continue;
            }

            Person person = createPersonFromUser(user);
            Person saved = personRepo.save(person);

            //we shouldn't make this work around library, but error is throw in library if we use it as intended
            //we should call userService.update
            WithUserPatchRequestBody body = new WithUserPatchRequestBody();
            body.setExternalId(saved.getId().toString());
            userService.getHttpClient().users().byUser_id(user.getId()).patch(body);
        }
    }

    private Person createPersonFromUser(User user) {
        Person person = new Person();
        person.setClerkId(user.getId());
        person.setEmail(findPrimaryEmail(user));
        person.setName(user.getFirstName());
        person.setLastname(user.getLastName());
        return person;
    }

    private String findPrimaryEmail(User user) {
        List<EmailAddress> emails = user.getEmailAddresses();
        if (emails == null || emails.isEmpty()) {
            return null;
        }
        String primaryEmailId = user.getPrimaryEmailAddressId();
        Optional<EmailAddress> primaryEmail = emails.stream().filter(email -> Objects.equals(email.getId(), primaryEmailId)).findFirst();
        return primaryEmail.map(EmailAddress::getEmailAddress).orElse(null);
    }

}