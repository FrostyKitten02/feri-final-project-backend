package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.service.WorkPackageService;

@CrossOrigin
@RestController
@RequestMapping("/work-package")
@RequiredArgsConstructor
public class WorkPackageController {
    private final WorkPackageService workPackageService;
    @PostMapping
    public void createWorkPackage(@RequestBody CreateWorkPackageRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        workPackageService.crateWorkPackage(servletRequest, request);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
    }

}
