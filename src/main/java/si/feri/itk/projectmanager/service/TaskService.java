package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreateTaskRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.TaskRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;
    private final WorkPackageRepo workPackageRepo;
    private final ProjectRepo projectRepo;
    public UUID createTask(CreateTaskRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserId(servletRequest);
        if (StringUtil.isNullOrEmpty(userId)) {
            log.warn("Unauthorized user tried to create a project");
            //this should never happen, we have a big problem if it does!
            throw new BadRequestException("User is not logged in");
        }

        if (request.getWorkPackageId() == null) {
            throw new BadRequestException("Work package id is required");
        }

        Optional<WorkPackage> workPackage = workPackageRepo.findById(request.getWorkPackageId());
        if (workPackage.isEmpty()) {
            throw new ItemNotFoundException("Work package not found");
        }

        Optional<Project> project = projectRepo.findById(workPackage.get().getProjectId());
        if (project.isEmpty() || !project.get().getOwnerId().equals(userId)) {
            throw new ItemNotFoundException("Work package not found");
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

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setWorkPackageId(request.getWorkPackageId());
        task.setIsRelevant(request.getIsRelevant());
        return taskRepo.save(task).getId();
    }
}
