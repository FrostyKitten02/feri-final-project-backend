package si.feri.itk.projectmanager.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.clerk.UserCreatedEvent;
import si.feri.itk.projectmanager.dto.clerk.UserDeletedEvent;
import si.feri.itk.projectmanager.dto.clerk.UserUpdatedEvent;
import si.feri.itk.projectmanager.service.ClerkService;
import si.feri.itk.projectmanager.util.ClerkEventUtil;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/clerk")
@RequiredArgsConstructor
public class ClerkController {
    private final ClerkService clerkService;
    @PostMapping("webhook/user-created")
    public void userCreated(@RequestBody UserCreatedEvent body) {
        ClerkEventUtil.validateEvent(body);
        clerkService.processUserCreatedEvent(body);
    }

    @PostMapping("webhook/user-updated")
    public void userUpdated(@RequestBody UserUpdatedEvent body) {
        ClerkEventUtil.validateEvent(body);
        clerkService.processUserUpdatedEvent(body);
    }

    @PostMapping("webhook/user-deleted")
    public void userDeleted(@RequestBody UserDeletedEvent body) {
        ClerkEventUtil.validateEvent(body);
        clerkService.processUserDeletedEvent(body);
    }
}
