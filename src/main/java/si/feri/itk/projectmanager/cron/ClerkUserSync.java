package si.feri.itk.projectmanager.cron;

import io.github.zzhorizonzz.client.models.EmailAddress;
import io.github.zzhorizonzz.client.models.User;
import io.github.zzhorizonzz.sdk.ClerkClient;
import io.github.zzhorizonzz.sdk.user.UserService;
import io.github.zzhorizonzz.sdk.user.request.ListAllUsersRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import si.feri.itk.projectmanager.model.Person;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClerkUserSync {
    private final ClerkClient clerkClient;
    private final PersonRepo personRepo;

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
        List<Person> persons = new ArrayList<>(users.size());

        for (User user : users) {
            if (!StringUtil.isNullOrEmpty(user.getExternalId())) {
                //we don't update our persons with external data
                //we could probably skip users after we find one with external id because we order by created_at
                continue;
            }

            Person person = new Person();
            person.setClerkId(user.getId());
            person.setEmail(findPrimaryEmail(user));
            person.setName(user.getFirstName());
            person.setLastname(user.getLastName());
            persons.add(person);
        }

        personRepo.saveAll(persons);
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