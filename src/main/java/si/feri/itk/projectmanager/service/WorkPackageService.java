package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.WorkPackageServiceUtil;

import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkPackageService {
    private final WorkPackageRepo workPackageRepo;
    private final ProjectRepo projectRepo;
    public UUID crateWorkPackage(HttpServletRequest servletRequest, CreateWorkPackageRequest request) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        Project project = projectRepo
                .findByIdAndOwnerId(request.getProjectId(), userId)
                .orElseThrow(()->new ItemNotFoundException("Project not found"));

        WorkPackage workPackage = WorkPackageServiceUtil.createWorkPackage(request, project.getId());
        WorkPackageServiceUtil.validateWorkPackageDateBounds(workPackage, project);
        return workPackageRepo.save(workPackage).getId();
    }



}
