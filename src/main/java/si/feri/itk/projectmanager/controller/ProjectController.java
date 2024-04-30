package si.feri.itk.projectmanager.controller;

import io.github.zzhorizonzz.client.models.User;
import io.github.zzhorizonzz.sdk.ClerkClient;
import io.github.zzhorizonzz.sdk.user.request.ListAllUsersRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.response.GetProjectResponse;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.ProjectService;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ClerkClient clerkClient;
    @PostMapping
    public ResourceCreatedResponse createProject(@RequestBody CreateProjectRequest request, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        UUID project = projectService.createProject(request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setId(project);
        return response;
    }

    @GetMapping("/{projectId}")
    public GetProjectResponse getProject(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        ProjectDto project = projectService.getProjectById(projectId, servletRequest);

        GetProjectResponse response = new GetProjectResponse();
        response.setProjectDto(project);
        return response;
    }

}
