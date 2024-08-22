package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.clerk.ClerkUserDeleted;
import si.feri.itk.projectmanager.dto.clerk.UserCreatedEvent;
import si.feri.itk.projectmanager.dto.clerk.UserDeletedEvent;
import si.feri.itk.projectmanager.dto.clerk.UserUpdatedEvent;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.util.StringUtil;
import si.feri.itk.projectmanager.util.service.PersonUtil;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClerkService {
    private final PersonRepo personRepo;

    //this method does not work correctly if we don't have user deleted endpoint, it will create new person with same email
    public void processUserCreatedEvent(UserCreatedEvent event) {
        final String email = PersonUtil.getEmailFromClerkUser(event.getUser());
        if (StringUtil.isNullOrEmpty(email)) {
            log.error("Email not found on new user clerk webhook event");
            throw new BadRequestException("Missing email on new user");
        }

        List<Person> people = personRepo.findAllByEmailAndClerkIdIsNull(email);
        if (people.size() > 1) {
            log.error("Multiple people found with email: " + email);
            throw new InternalServerException("Multiple people found");
        }

        if (!people.isEmpty()) {
            Person person = people.get(0);
            PersonUtil.updateNewPersonWithClerkUser(person, event.getUser());
            personRepo.save(person);
            return;
        }

        //if save throws then we probably have person with same email added
        Person newPerson = PersonUtil.createNewPersonFromClerkUser(event.getUser());
        personRepo.save(newPerson);
    }

    public void processUserUpdatedEvent(UserUpdatedEvent event) {
        final String clerkId = event.getUser().getId();
        Optional<Person> personOptional = personRepo.findByClerkId(clerkId);

        if (personOptional.isEmpty()) {
            //should we just create new person and log this, because if this happens then there must be something wrong with create event handling
            log.error("Person not found with clerkId: " + clerkId);
            throw new BadRequestException("Person not found");
        }

        Person person = personOptional.get();
        PersonUtil.updateExistingPersonWithClerkUser(person, event.getUser());
        personRepo.save(person);
    }

    public void processUserDeletedEvent(UserDeletedEvent event) {
        ClerkUserDeleted clerkUserDeleted = event.getUser();
        final String deletedId = clerkUserDeleted.getId();

        if (StringUtil.isNullOrEmpty(deletedId)) {
            log.error("Deleted id not found on clerk user deleted event");
            throw new BadRequestException("Missing user id");
        }

        Optional<Person> personOptional = personRepo.findByClerkId(deletedId);
        if (personOptional.isEmpty()) {
            //maybe we don't return error here, because if this happens this means or database is at fault, meaning we don't have data synced correctly
            log.error("Person not found with clerkId: " + deletedId);
            throw new InternalServerException("Person not found");
        }

        Person person = personOptional.get();
        PersonUtil.updatePersonDeletedUser(person);
        personRepo.save(person);
    }

}
