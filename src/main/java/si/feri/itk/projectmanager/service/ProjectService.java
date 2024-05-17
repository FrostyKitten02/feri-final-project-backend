package si.feri.itk.projectmanager.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.dto.request.ProjectListSearchParams;
import si.feri.itk.projectmanager.dto.response.ListProjectResponse;
import si.feri.itk.projectmanager.dto.sortinforequest.ProjectSortInfoRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.ProjectMapper;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.ProjectList;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.repository.PersonOnProjectRepo;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.ProjectListRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.ProjectServiceUtil;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final PersonRepo personRepo;
    private final ProjectRepo projectRepo;
    private final ProjectListRepo projectListRepo;
    private final PersonOnProjectRepo personOnProjectRepo;
    public UUID createProject(CreateProjectRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        ProjectServiceUtil.validateCreateProjectRequest(request);
        Project project = ProjectServiceUtil.createNewProject(request, userId);
        return projectRepo.save(project).getId();
    }

    public ProjectDto getProjectById(UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        //we may not want to check if owner made the call but for now it's ok, when we allow other users to see projects we have to check if they can see it!!!
        Optional<Project> project = projectRepo.findByIdAndOwnerId(projectId, userId);
        return project.map(ProjectMapper.INSTANCE::toDto).orElseThrow( () -> new ItemNotFoundException("Project not found"));
    }

    public void addPersonToProject(UUID projectId, AddPersonToProjectRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        ProjectServiceUtil.validateAddPersonToProjectRequest(projectId, request);
        PersonOnProject personOnProject = createPersonOnProject(projectId, userId, request.getPersonId());
        personOnProjectRepo.save(personOnProject);
    }

    private PersonOnProject createPersonOnProject(UUID projectId, String ownerId, UUID personId) {
        Project project = projectRepo.findByIdAndOwnerId(projectId, ownerId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        Person person = personRepo.findById(personId).orElseThrow(() -> new ItemNotFoundException("Person not found"));

        Optional<PersonOnProject> personOnProjectDb = personOnProjectRepo.findFirstByProjectIdAndPersonId(project.getId(), person.getId());

        if (personOnProjectDb.isPresent()) {
            throw new BadRequestException("Person is already on project");
        }

        return ProjectServiceUtil.createNewPersonOnProject(project, person);
    }

    public ListProjectResponse searchUsersProjects(PageInfoRequest pageInfoRequest, ProjectSortInfoRequest sortInfoRequest, ProjectListSearchParams searchParams, HttpServletRequest servletRequest) {
        //todo use search params!!!
        SortInfo<?> sort = RequestUtil.getSortInfoFromRequest(sortInfoRequest);
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        Page<ProjectList> projectsPage = projectListRepo.findAllByOwnerId(userId, PageInfo.toPageRequest(pageInfoRequest, sort));
        return ListProjectResponse.fromPage(projectsPage);
    }

}
