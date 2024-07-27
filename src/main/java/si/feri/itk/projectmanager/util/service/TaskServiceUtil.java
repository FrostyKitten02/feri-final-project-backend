package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.task.CreateTaskRequest;
import si.feri.itk.projectmanager.dto.request.task.UpdateTaskRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.util.DateUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.time.LocalDate;

public class TaskServiceUtil {
    private TaskServiceUtil() {}

    public static void updateTask(Task task, WorkPackage taskWp, UpdateTaskRequest request) {
        if (!task.getWorkPackageId().equals(taskWp.getId())) {
            throw new InternalServerException("Task work package id does not match work package id");
        }

        DateUtil.validateDurationStrict(request);

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getIsRelevant() != null) {
            task.setIsRelevant(request.getIsRelevant());
        }

        if (request.getStartDate() != null) {
            task.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            task.setEndDate(request.getEndDate());
        }

        validateTaskBounds(task, taskWp);
    }

    public static void validateCreateTaskRequest(CreateTaskRequest request) {
        if (request.getWorkPackageId() == null) {
            throw new BadRequestException("Work package id is required");
        }

        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw new BadRequestException("Title is required");
        }

        if (request.getIsRelevant() == null) {
            throw new BadRequestException("IsRelevant is required");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new BadRequestException("End date is required");
        }

        DateUtil.validateDurationStrict(request);
    }

    public static void validateTaskBounds(Task task, WorkPackage workPackage) {
        LocalDate taskStartDate = task.getStartDate();
        LocalDate taskEndDate = task.getEndDate();
        if (taskStartDate.isAfter(workPackage.getEndDate()) || taskStartDate.isBefore(workPackage.getStartDate())) {
            throw new BadRequestException("Task start date must be between work package start and end date");
        }

        if (taskEndDate.isAfter(workPackage.getEndDate()) || taskEndDate.isBefore(workPackage.getStartDate())) {
            throw new BadRequestException("Task end date must be between work package start and end date");
        }
    }

    public static Task createNewTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setWorkPackageId(request.getWorkPackageId());
        task.setIsRelevant(request.getIsRelevant());
        return task;
    }

}
