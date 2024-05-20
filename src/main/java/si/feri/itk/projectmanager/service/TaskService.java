package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreateTaskRequest;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.TaskRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.TaskServiceUtil;

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
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        TaskServiceUtil.validateCreateTaskRequest(request);
        WorkPackage workPackage = getWorkPackageAndValidateOwner(request.getWorkPackageId(), userId);

        Task task = TaskServiceUtil.createNewTask(request);
        TaskServiceUtil.validateTaskBounds(task, workPackage);
        return taskRepo.save(task).getId();
    }

    private WorkPackage getWorkPackageAndValidateOwner(UUID workPackageId, String ownerId) {
        Optional<WorkPackage> workPackage = workPackageRepo.findById(workPackageId);
        if (workPackage.isEmpty()) {
            throw new ItemNotFoundException("Work package not found");
        }

        Optional<Project> project = projectRepo.findById(workPackage.get().getProjectId());
        if (project.isEmpty() || !project.get().getOwnerId().equals(ownerId)) {
            throw new ItemNotFoundException("Work package not found");
        }

        return workPackage.get();
    }
}
