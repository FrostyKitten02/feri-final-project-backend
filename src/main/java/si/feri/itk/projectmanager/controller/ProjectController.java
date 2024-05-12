package si.feri.itk.projectmanager.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.dto.request.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.request.ProjectListSearchParams;
import si.feri.itk.projectmanager.dto.response.GetPeopleResponse;
import si.feri.itk.projectmanager.dto.response.GetProjectResponse;
import si.feri.itk.projectmanager.dto.response.ListProjectResponse;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.dto.sortinforequest.ProjectSortInfoRequest;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.service.PersonService;
import si.feri.itk.projectmanager.service.ProjectService;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final PersonService personService;
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

    @PostMapping("{projectId}/add-person-to-project/")
    public void addPersonToProject(@PathVariable UUID projectId, @RequestBody AddPersonToProjectRequest request, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        projectService.addPersonToProject(projectId, request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
    }

    @GetMapping("/{projectId}/people")
    public GetPeopleResponse getPeopleOnProjectByProjectId(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        List<PersonDto> people = personService.findPeopleOnProject(projectId);
        GetPeopleResponse response = new GetPeopleResponse();
        response.setPeople(people);
        response.setProjectId(projectId);
        return response;
    }


    @GetMapping("/list")
    public ListProjectResponse listProjects(
            @NotNull PageInfoRequest pageInfo,
            @Nullable ProjectSortInfoRequest sortInfo,
            @Nullable ProjectListSearchParams searchParams,
            HttpServletRequest servletRequest
    ) {
        return projectService.searchUsersProjects(pageInfo, sortInfo, searchParams, servletRequest);
    }

}
