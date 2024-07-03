package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.common.IDuration;
import si.feri.itk.projectmanager.dto.request.workpackage.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.dto.request.workpackage.UpdateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.util.DateUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.time.LocalDate;
import java.util.UUID;

public class WorkPackageServiceUtil {
    private WorkPackageServiceUtil() {}

    public static void validateUpdateWorkPackageRequest(UpdateWorkPackageRequest request) {
        DateUtil.validateDurationStrict(request);

        if (request.getAssignedPM() != null && request.getAssignedPM() <= 0) {
            throw new BadRequestException("Assigned PM must be a positive number");
        }
    }

    public static void updateWorkPackage(WorkPackage wp, IDuration wpTaskBounds, Project wpProject, UpdateWorkPackageRequest request) {
        if (!wp.getProjectId().equals(wpProject.getId())) {
            throw new InternalServerException("Work package and project do not match");
        }

        if (request.getTitle() != null) {
            wp.setTitle(request.getTitle());
        }

        if (request.getStartDate() != null) {
            wp.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            wp.setEndDate(request.getEndDate());
        }

        if (request.getIsRelevant() != null) {
            wp.setIsRelevant(request.getIsRelevant());
        }

        if (request.getAssignedPM() != null) {
            wp.setAssignedPM(request.getAssignedPM());
        }

        validateWorkPackageDateBounds(wp, wpProject);
        validateWorkPackageDateBoundsFromTasksDuration(wp, wpTaskBounds);
    }



    private static void validateWorkPackageDateBoundsFromTasksDuration(WorkPackage wp, IDuration taskBounds) {
        if (taskBounds == null) {
            return;
        }

        if (taskBounds.getStartDate() != null && taskBounds.getStartDate().isBefore(wp.getStartDate())) {
            throw new BadRequestException("Work package start date must be after work package tasks start date");
        }

        if (taskBounds.getEndDate() != null && taskBounds.getEndDate().isAfter(wp.getEndDate())) {
            throw new BadRequestException("Work package end date must be before work package tasks end date");
        }
    }

    public static void validateCreateWorkPackageRequest(CreateWorkPackageRequest request) {
        if (request.getProjectId() == null) {
            throw new BadRequestException("ProjectId is required");
        }

        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw new BadRequestException("Title is required");
        }

        if (request.getIsRelevant() == null) {
            throw new BadRequestException("IsRelevant is required");
        }

        if (request.getAssignedPM() == null || request.getAssignedPM() <= 0) {
            throw new BadRequestException("Assigned PM is required, it must be a positive number");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new BadRequestException("End date is required");
        }

        DateUtil.validateDurationStrict(request);
    }

    public static void validateWorkPackageDateBounds(WorkPackage wp, Project project) {
        LocalDate taskStartDate = wp.getStartDate();
        LocalDate taskEndDate = wp.getEndDate();
        if (taskStartDate.isAfter(project.getEndDate()) || taskStartDate.isBefore(project.getStartDate())) {
            throw new BadRequestException("Work package start date must be between project start and end date");
        }

        if (taskEndDate.isAfter(project.getEndDate()) || taskEndDate.isBefore(project.getStartDate())) {
            throw new BadRequestException("Work package end date must be between project start and end date");
        }
    }

    public static WorkPackage createWorkPackage(CreateWorkPackageRequest request, UUID projectId) {
        WorkPackage workPackage = new WorkPackage();
        workPackage.setTitle(request.getTitle());
        workPackage.setStartDate(request.getStartDate());
        workPackage.setIsRelevant(request.getIsRelevant());
        workPackage.setEndDate(request.getEndDate());
        workPackage.setProjectId(projectId);
        workPackage.setAssignedPM(request.getAssignedPM());
        return workPackage;
    }

}
