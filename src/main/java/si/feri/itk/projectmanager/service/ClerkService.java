package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.clerk.UserCreatedEvent;
import si.feri.itk.projectmanager.repository.PersonRepo;

@Service
@RequiredArgsConstructor
public class ClerkService {
    private final PersonRepo personRepo;
    public void processUserCreatedEvent(UserCreatedEvent userCreatedEvent) {

    }

}
