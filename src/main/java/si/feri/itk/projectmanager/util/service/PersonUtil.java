package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.clerk.ClerkUser;
import si.feri.itk.projectmanager.dto.clerk.EmailAddress;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.util.StringUtil;

public class PersonUtil {
    private PersonUtil() {
    }

    public static void updateNewPersonWithClerkUser(Person person, ClerkUser clerkUser) {
        person.setName(clerkUser.getFirstName());
        person.setLastname(clerkUser.getLastName());
        person.setClerkId(clerkUser.getId());
        person.setProfileImageUrl(clerkUser.getImageUrl());
    }

    public static Person createNewPersonFromClerkUser(ClerkUser clerkUser) {
        Person person = new Person();
        person.setClerkId(clerkUser.getId());
        person.setName(clerkUser.getFirstName());
        person.setLastname(clerkUser.getLastName());
        person.setProfileImageUrl(clerkUser.getImageUrl());
        final String email = getEmailFromClerkUser(clerkUser);
        if (StringUtil.isNullOrEmpty(email)) {
            throw new BadRequestException("Email not found on create user clerk webhook event");
        }
        person.setEmail(email);
        return person;
    }

    public static String getEmailFromClerkUser(ClerkUser user) {
        final String mainEmail = user.getPrimaryEmailAddressId();

        if (mainEmail == null) {
            return null;
        }

        for (EmailAddress emailAddress : user.getEmailAddresses()) {
            if (emailAddress != null && emailAddress.getId().equals(mainEmail)) {
                return emailAddress.getEmailAddress();
            }
        }

        return null;
    }

    public static void updateExistingPersonWithClerkUser(Person person, ClerkUser clerkUser) {
        person.setName(clerkUser.getFirstName());
        person.setLastname(clerkUser.getLastName());
        person.setProfileImageUrl(clerkUser.getImageUrl());
        final String email = getEmailFromClerkUser(clerkUser);
        if (StringUtil.isNullOrEmpty(email)) {
            throw new BadRequestException("Email not found on updated user clerk webhook event");
        }
        person.setEmail(email);
    }

    public static void updatePersonDeletedUser(Person person) {
        person.setClerkId(null);
        person.setProfileImageUrl(null);
    }
}
