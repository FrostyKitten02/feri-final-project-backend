package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.AddPersonToTaskRequest;
import si.feri.itk.projectmanager.dto.request.CreateTaskRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.PersonOnTask;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.util.StringUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TaskServiceUtil {
    private TaskServiceUtil() {}

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

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }
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

    public static void validateAddPersonToTask(UUID taskId, UUID personId, AddPersonToTaskRequest request) {
        if (taskId == null) {
            throw new BadRequestException("Task id is required");
        }

        if (personId == null) {
            throw new BadRequestException("Person id is required");
        }

        BigDecimal occupancy = request.getOccupancy();
        if (occupancy == null) {
            throw new BadRequestException("Occupation is required");
        }

        if (occupancy.compareTo(BigDecimal.ONE) >= 1) {
            throw new BadRequestException("Occupancy must be less than or equal to 1");
        }

        if (occupancy.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Occupancy must be greater than 0");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() != null && request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("End date must be after start date");
        }

    }

    public static PersonOnTask createNewPersonOnTask(UUID taskId, UUID personId, AddPersonToTaskRequest request, LocalDate taskEndDate) {
        PersonOnTask personOnTask = new PersonOnTask();
        personOnTask.setTaskId(taskId);
        personOnTask.setPersonId(personId);
        personOnTask.setOccupancy(request.getOccupancy());
        personOnTask.setStartDate(request.getStartDate());
        if (request.getEndDate() == null) {
            personOnTask.setEndDate(taskEndDate);
        } else {
            personOnTask.setEndDate(request.getEndDate());
        }
        return personOnTask;
    }

    public static void validatePersonOnTaskBounds(PersonOnTask personOnTask, Task task) {
        LocalDate personOnTaskStartDate = personOnTask.getStartDate();
        LocalDate personOnTaskEndDate = personOnTask.getEndDate();
        if (personOnTaskStartDate.isAfter(task.getEndDate()) || personOnTaskStartDate.isBefore(task.getStartDate())) {
            throw new BadRequestException("Task start date must be between work package start and end date");
        }

        if (personOnTaskEndDate.isAfter(task.getEndDate()) || personOnTaskEndDate.isBefore(task.getStartDate())) {
            throw new BadRequestException("Task end date must be between work package start and end date");
        }
    }
}
