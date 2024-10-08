package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import si.feri.itk.projectmanager.dto.common.Duration;
import si.feri.itk.projectmanager.dto.common.IDuration;
import si.feri.itk.projectmanager.dto.request.workpackage.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.dto.request.workpackage.UpdateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.TaskRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.WorkPackageServiceUtil;

import java.time.LocalDate;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkPackageService {
    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private final WorkPackageRepo workPackageRepo;
    public UUID crateWorkPackage(@Validated CreateWorkPackageRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        Project project = projectRepo
                .findByIdAndOwnerId(request.getProjectId(), userId)
                .orElseThrow(()->new ItemNotFoundException("Project not found"));

        WorkPackage workPackage = WorkPackageServiceUtil.createWorkPackage(request, project.getId());
        WorkPackageServiceUtil.validateWorkPackageDateBounds(workPackage, project);
        return workPackageRepo.save(workPackage).getId();
    }

    public void deleteWorkPackage(UUID workPackageId, HttpServletRequest request) {
        if (workPackageId == null) {
            throw new ItemNotFoundException("Work package not found");
        }

        final String userId = RequestUtil.getUserIdStrict(request);
        projectRepo.findProjectByWorkPackageIdAndOwnerId(workPackageId, userId).orElseThrow(()->new ItemNotFoundException("Work package not found"));
        final WorkPackage workPackage = workPackageRepo.findById(workPackageId).orElseThrow(()->new ItemNotFoundException("Work package not found"));
        workPackageRepo.delete(workPackage);
    }

    public void updateWorkPackage(UUID workPackageId, @Validated UpdateWorkPackageRequest request, HttpServletRequest servletRequest) {
        final String userId = RequestUtil.getUserIdStrict(servletRequest);
        final Project project = projectRepo.findProjectByWorkPackageIdAndOwnerId(workPackageId, userId).orElseThrow(()->new ItemNotFoundException("Work package not found"));
        final WorkPackage workPackage = workPackageRepo.findById(workPackageId).orElseThrow(()->new ItemNotFoundException("Work package not found"));

        WorkPackageServiceUtil.validateUpdateWorkPackageRequest(request);
        WorkPackageServiceUtil.updateWorkPackage(workPackage, createWorkPackageTasksDuration(workPackageId), project, request);

        workPackageRepo.save(workPackage);
    }

    private IDuration createWorkPackageTasksDuration(UUID workPackageId) {
        LocalDate start = taskRepo.findEarliestDateByWorkPackageId(workPackageId).orElse(null);
        LocalDate end = taskRepo.findLatestDateByWorkPackageId(workPackageId).orElse(null);
        return new Duration(start, end);
    }

}