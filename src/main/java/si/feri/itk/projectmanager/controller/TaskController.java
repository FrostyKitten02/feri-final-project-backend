package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable UUID taskId, HttpServletRequest servletRequest) {
        taskService.deleteTask(taskId, servletRequest);
    }

    @PatchMapping("/{taskId}")
    public void updateTask(@PathVariable UUID taskId, @RequestBody UpdateTaskRequest request, HttpServletRequest servletRequest) {
        taskService.updateTask(taskId, request, servletRequest);
    }

}
