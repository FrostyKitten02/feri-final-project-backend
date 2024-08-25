package si.feri.itk.projectmanager.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.dto.model.ProjectFileDto;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;
import si.feri.itk.projectmanager.dto.request.project.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.ProjectListSearchParams;
import si.feri.itk.projectmanager.dto.request.project.ProjectSortInfoRequest;
import si.feri.itk.projectmanager.dto.request.project.UpdateProjectRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.dto.response.person.GetPeopleResponse;
import si.feri.itk.projectmanager.dto.response.project.GetProjectResponse;
import si.feri.itk.projectmanager.dto.response.project.ListProjectResponse;
import si.feri.itk.projectmanager.dto.response.project.ProjectFilesResponse;
import si.feri.itk.projectmanager.dto.response.project.ProjectListStatusResponse;
import si.feri.itk.projectmanager.dto.response.project.UpdateProjectResponse;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.model.project.ProjectFile;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.service.FileService;
import si.feri.itk.projectmanager.service.PersonService;
import si.feri.itk.projectmanager.service.ProjectService;
import si.feri.itk.projectmanager.util.service.FileServiceUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


//TODO when project is added or edited edit project starting soon email queue!!!
//on edit only if start date is changed, also check if we even need to send new email based on new start date

@CrossOrigin
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final PersonService personService;
    private final FileService fileService;

    @PostMapping
    public ResourceCreatedResponse createProject(@RequestBody CreateProjectRequest request, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        UUID project = projectService.createProject(request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setId(project);
        return response;
    }

    @PatchMapping("/{projectId}")
    public UpdateProjectResponse updateProject(@PathVariable UUID projectId, @RequestBody UpdateProjectRequest request, HttpServletRequest servletRequest) {
        ProjectDto project = projectService.updateProject(projectId, request, servletRequest);
        UpdateProjectResponse response = new UpdateProjectResponse();
        response.setProject(project);
        return response;
    }

    @GetMapping("/{projectId}")
    public GetProjectResponse getProject(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        ProjectDto project = projectService.getProjectById(projectId, servletRequest);

        GetProjectResponse response = new GetProjectResponse();
        response.setProjectDto(project);
        return response;
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        projectService.deleteProject(projectId, servletRequest);
    }

    @PostMapping("{projectId}/add-person-to-project/")
    public void addPersonToProject(@PathVariable UUID projectId, @RequestBody @Valid AddPersonToProjectRequest request, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        projectService.addPersonToProject(projectId, request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @DeleteMapping("{projectId}/remove-person/{personId}")
    public void removePersonFromProject(@PathVariable UUID projectId, @PathVariable UUID personId, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        projectService.removePersonFromProject(projectId, personId, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @GetMapping("/{projectId}/people")
    public GetPeopleResponse getPeopleOnProjectByProjectId(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        List<PersonDto> people = personService.findPeopleOnProject(projectId, servletRequest);
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

    @GetMapping("/{projectId}/statistics")
    public ProjectStatisticsResponse getProjectStatistics(
            @PathVariable @NotNull UUID projectId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) Integer monthsNumber,
            HttpServletRequest servletRequest
    ) {
        return projectService.getProjectStatistics(projectId, from, monthsNumber, servletRequest);
    }

    @GetMapping("/list/status")
    public ProjectListStatusResponse listProjectsStatus(HttpServletRequest servletRequest) {
        return projectService.listProjectsStatus(servletRequest);
    }



    @RequestMapping(
            path = "{projectId}/upload-file",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResourceCreatedResponse uploadProjectFile(
            @RequestBody MultipartFile[] files,
            @PathVariable UUID projectId,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        List<UUID> created = fileService.uploadProjectFiles(files, projectId, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);

        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setIds(created);
        return response;
    }

    @GetMapping( "file/{projectFileId}")
    public ResponseEntity<Resource> download(@PathVariable UUID projectFileId, HttpServletRequest servletRequest) {
        ProjectFile projectFile = fileService.getProjectFile(projectFileId, servletRequest);
        Resource resource = fileService.getProjectFileResource(projectFile);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + projectFile.getOriginalFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("file/{projectFileId}")
    public void deleteProjectFile(@PathVariable UUID projectFileId, HttpServletRequest servletRequest) {
        fileService.deleteProjectFile(projectFileId, servletRequest);
    }

    @GetMapping("{projectId}/files")
    public ProjectFilesResponse getProjectFiles(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        List<ProjectFileDto> files = fileService.getAllProjectFiles(projectId, servletRequest);

        ProjectFilesResponse response = new ProjectFilesResponse();
        response.setFiles(files);
        return response;
    }
}