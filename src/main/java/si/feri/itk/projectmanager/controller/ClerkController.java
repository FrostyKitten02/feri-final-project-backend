package si.feri.itk.projectmanager.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.clerk.UserCreatedEvent;
import si.feri.itk.projectmanager.service.ClerkService;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/clerk")
@RequiredArgsConstructor
public class ClerkController {
    private final ClerkService clerkService;
    @PostMapping("webhook/user-created")
    public void userCreated(@RequestBody UserCreatedEvent body, HttpServletRequest servletRequest) {;
        clerkService.processUserCreatedEvent(body);
    }
}
