package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.workpackage.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.dto.request.workpackage.UpdateWorkPackageRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.WorkPackageService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/work-package")
@RequiredArgsConstructor
public class WorkPackageController {
    private final WorkPackageService workPackageService;
    @PostMapping
    public ResourceCreatedResponse createWorkPackage(@RequestBody CreateWorkPackageRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        UUID workPackageId = workPackageService.crateWorkPackage(servletRequest, request);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setId(workPackageId);
        return response;
    }

    @DeleteMapping("/{workPackageId}")
    public void deleteWorkPackage(@PathVariable UUID workPackageId, HttpServletRequest servletRequest) {
        workPackageService.deleteWorkPackage(workPackageId, servletRequest);
    }

    @PatchMapping("/{workPackageId}")
    public void updateWorkPackage(@PathVariable UUID workPackageId, @RequestBody UpdateWorkPackageRequest request, HttpServletRequest servletRequest) {
        workPackageService.updateWorkPackage(workPackageId, request, servletRequest);
    }

}
