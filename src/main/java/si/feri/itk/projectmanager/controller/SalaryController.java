package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.CreateSalaryRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.SalaryService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;
    @PostMapping
    public ResourceCreatedResponse createSalaryForPerson(@RequestBody CreateSalaryRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        ResourceCreatedResponse response = new ResourceCreatedResponse();
        UUID salaryId = salaryService.addSalaryToPerson(request, servletRequest);
        response.setId(salaryId);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        return response;
    }
}
