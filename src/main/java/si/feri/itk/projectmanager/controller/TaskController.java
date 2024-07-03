package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.task.AddPersonToTaskRequest;
import si.feri.itk.projectmanager.dto.request.task.CreateTaskRequest;
import si.feri.itk.projectmanager.dto.request.task.UpdateTaskRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.TaskService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PostMapping
    public ResourceCreatedResponse createTask(@RequestBody CreateTaskRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        UUID taskId = taskService.createTask(request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);

        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setId(taskId);
        return response;
    }

    @PatchMapping("/{taskId}")
    public void updateTask(@PathVariable UUID taskId, @RequestBody UpdateTaskRequest request, HttpServletRequest servletRequest) {
        taskService.updateTask(taskId, request, servletRequest);
    }

    @PostMapping("/{taskId}/assign-person/{personId}")
    public void assignPersonToTask(
            @PathVariable UUID taskId,
            @PathVariable UUID personId,
            @RequestBody AddPersonToTaskRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        taskService.addPersonToTask(taskId, personId, request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
