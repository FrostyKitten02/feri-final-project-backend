package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.AddPersonToTaskRequest;
import si.feri.itk.projectmanager.dto.request.CreateTaskRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.TaskService;

import java.math.BigDecimal;
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
