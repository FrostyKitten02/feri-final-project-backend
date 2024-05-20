package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.util.StringUtil;

import java.time.LocalDate;
import java.util.UUID;

public class WorkPackageServiceUtil {
    private WorkPackageServiceUtil() {}

    public static void validateCreateWorkPackageRequest(CreateWorkPackageRequest request) {
        if (request.getProjectId() == null) {
            throw new BadRequestException("ProjectId is required");
        }

        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw new BadRequestException("Title is required");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new BadRequestException("End date is required");
        }

        if (!request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }

        if (request.getIsRelevant() == null) {
            throw new BadRequestException("IsRelevant is required");
        }
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
        return workPackage;
    }

}
