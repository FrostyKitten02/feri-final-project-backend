package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.common.Duration;
import si.feri.itk.projectmanager.dto.common.IDuration;
import si.feri.itk.projectmanager.dto.request.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.dto.request.UpdateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.Project;
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

    public void updateWorkPackage(UUID workPackageId, UpdateWorkPackageRequest request, HttpServletRequest servletRequest) {
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
