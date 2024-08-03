package si.feri.itk.projectmanager.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.common.Duration;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;
import si.feri.itk.projectmanager.dto.request.project.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.ProjectListSearchParams;
import si.feri.itk.projectmanager.dto.response.project.ListProjectResponse;
import si.feri.itk.projectmanager.dto.response.project.ProjectListStatusResponse;
import si.feri.itk.projectmanager.dto.request.project.UpdateProjectRequest;
import si.feri.itk.projectmanager.dto.response.statistics.PersonWorkDto;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectMonthDto;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.dto.request.project.ProjectSortInfoRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.PersonMapper;
import si.feri.itk.projectmanager.mapper.ProjectMapper;
import si.feri.itk.projectmanager.model.Occupancy;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.ProjectBudgetSchema;
import si.feri.itk.projectmanager.model.ProjectList;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;
import si.feri.itk.projectmanager.model.person.Salary;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.repository.OccupancyRepo;
import si.feri.itk.projectmanager.repository.PersonOnProjectRepo;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.ProjectBudgetSchemaRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.repository.projectlist.ProjectListRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.SalaryRepo;
import si.feri.itk.projectmanager.util.ProjectBudgetUtil;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StatisticUtil;
import si.feri.itk.projectmanager.util.service.ProjectServiceUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final PersonRepo personRepo;
    private final SalaryRepo salaryRepo;
    private final ProjectRepo projectRepo;
    private final OccupancyRepo occupancyRepo;
    private final WorkPackageRepo workPackageRepo;
    private final ProjectListRepo projectListRepo;
    private final PersonOnProjectRepo personOnProjectRepo;
    private final ProjectBudgetSchemaRepo projectBudgetSchemaRepo;
    public UUID createProject(CreateProjectRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        ProjectServiceUtil.validateCreateProjectRequest(request);

        ProjectBudgetSchema schema = projectBudgetSchemaRepo.findById(request.getProjectBudgetSchemaId()).orElseThrow(()-> new ItemNotFoundException("Project budget schema not found"));
        BigDecimal indirectBudget = ProjectBudgetUtil.calculateIndirectBudget(request, schema);

        Project project = ProjectServiceUtil.createNewProject(request, userId, indirectBudget);
        return projectRepo.save(project).getId();
    }

    @Transactional
    public void deleteProject(UUID projectId, HttpServletRequest req) {
        if (projectId == null) {
            throw new BadRequestException("Project id is required");
        }

        final String userId = RequestUtil.getUserIdStrict(req);
        final Project project = projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        personOnProjectRepo.deleteAllByProjectId(projectId);
        occupancyRepo.deleteAllByProjectId(projectId);
        projectRepo.delete(project);
    }

    public ProjectDto updateProject(UUID projectId, UpdateProjectRequest request, HttpServletRequest servletRequest) {
        ProjectServiceUtil.validateUpdateProjectRequest(request, projectId);
        final String userId = RequestUtil.getUserIdStrict(servletRequest);
        final Project project = projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));

        final Duration wpDuration = createWpStartAndEndDates(projectId);
        final Duration occupancyDuration = createOccupancyStartAndEndDates(projectId);

        final ProjectBudgetSchema schema;
        if (request.getProjectBudgetSchemaId() != null) {
            schema = projectBudgetSchemaRepo.findById(request.getProjectBudgetSchemaId()).orElseThrow(() -> new ItemNotFoundException("Project budget schema not found"));
        } else {
            schema = projectBudgetSchemaRepo.findById(project.getProjectBudgetSchemaId()).orElseThrow(() -> new ItemNotFoundException("Project budget schema not found"));
        }

        ProjectServiceUtil.updateProject(request, project, schema, wpDuration, occupancyDuration);
        final Project savedProject = projectRepo.save(project);
        return ProjectMapper.INSTANCE.toDto(savedProject);
    }

    private Duration createWpStartAndEndDates(UUID projectId) {
        Duration wpDuration = new Duration();
        workPackageRepo.findFirstWorkPackageStartDateByProjectId(projectId)
                .ifPresent(wpDuration::setStartDate);
        workPackageRepo.findLastWorkPackageEndDateByProjectId(projectId)
                .ifPresent(wpDuration::setEndDate);
        return wpDuration;
    }

    private Duration createOccupancyStartAndEndDates(UUID projectId) {
        Duration occupancyDuration = new Duration();
        //we set dates we get from db to last day of month
        //we can add 1 month and remove 1 day bcs dates in this table SHOULD ALWAYS BE first day of the month
        occupancyRepo.findEarliestMonthByProjectId(projectId)
                .ifPresent(o->{
                    occupancyDuration.setStartDate(o.plusMonths(1).minusDays(1));
                });
        occupancyRepo.findLatestMonthByProjectId(projectId)
                .ifPresent(o->{
                    occupancyDuration.setEndDate(o.plusMonths(1).minusDays(1));
                });
        return occupancyDuration;
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

    @Transactional
    public void removePersonFromProject(UUID projectId, UUID personId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        ProjectServiceUtil.validateRemovePersonFromProject(projectId, personId);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        personRepo.findById(personId).orElseThrow(() -> new ItemNotFoundException("Person not found"));

        occupancyRepo.deleteAllByPersonIdAndProjectId(personId, projectId);
        personOnProjectRepo.deleteAllByPersonIdAndProjectId(personId, projectId);
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
        SortInfo<?> sort = RequestUtil.getSortInfoFromRequest(sortInfoRequest);
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        Page<ProjectList> projectsPage = projectListRepo.searchUsersProjects(searchParams, userId, PageInfo.toPageRequest(pageInfoRequest, sort));
        return ListProjectResponse.fromPage(projectsPage);
    }

    public ProjectStatisticsResponse getProjectStatistics(UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        Project project = projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        ProjectStatisticsResponse stats = StatisticUtil.calculateProjectStatistics(project);

        List<Person> people = personRepo.findAllByProjectId(projectId);
        Map<UUID, PersonDto> peopleMap = people.stream().collect(Collectors.toMap(Person::getId, PersonMapper.INSTANCE::toDto));
        stats.setPeople(peopleMap);
        calculateProjectSalaryStats(stats, people, projectId);
        return stats;
    }

    public ProjectListStatusResponse listProjectsStatus(HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        LocalDate today = LocalDate.now();

        PageInfo pageInfo = new PageInfo(1, 0L, 1);
        Pageable pageable = PageInfo.toPageRequest(pageInfo, null);

        ProjectListStatusResponse response = new ProjectListStatusResponse();
        ProjectListSearchParams searchParams = new ProjectListSearchParams();

        searchParams.setEndDateTo(today);
        Page<ProjectList> finishedProjects = projectListRepo.searchUsersProjects(searchParams, userId, pageable);
        response.setFinishedProjects(finishedProjects.getTotalElements());

        searchParams.setEndDateTo(null);
        searchParams.setEndDateFrom(today);
        searchParams.setStartDateTo(today);
        Page<ProjectList> inProgressProjects = projectListRepo.searchUsersProjects(searchParams, userId, pageable);
        response.setInProgressProjects(inProgressProjects.getTotalElements());

        searchParams.setEndDateFrom(null);
        searchParams.setStartDateTo(null);
        searchParams.setStartDateFrom(today.plusDays(1));
        Page<ProjectList> scheduledProjects = projectListRepo.searchUsersProjects(searchParams, userId, pageable);
        response.setScheduledProjects(scheduledProjects.getTotalElements());

        return response;
    }

    private void calculateProjectSalaryStats(ProjectStatisticsResponse response, List<Person> people, UUID projectId) {
        for (ProjectMonthDto month : response.getMonths()) {
            ArrayList<PersonWorkDto> personWorkDtos = new ArrayList<>(people.size());
            for (Person p : people) {
                PersonWorkDto personWorkDto = new PersonWorkDto();
                personWorkDto.setPersonId(p.getId());
                Optional<Occupancy> occupancyOpt = occupancyRepo.findByMonthAndPersonIdAndProjectId(month.getDate(), p.getId(), projectId);
                if (occupancyOpt.isPresent()) {
                    Occupancy occupancy = occupancyOpt.get();
                    personWorkDto.setOccupancyId(occupancy.getId());
                    personWorkDto.setTotalWorkPm(occupancy.getValue());
                } else {
                    personWorkDto.setOccupancyId(null);
                    personWorkDto.setTotalWorkPm(BigDecimal.ZERO);
                }
                personWorkDto.setAvgSalary(calculateAvgSalaryForMonth(month, p.getId()));
                month.addWorkPm(personWorkDto.getTotalWorkPm());
                month.addActualSpending(personWorkDto.getAvgSalary().multiply(personWorkDto.getTotalWorkPm()));

                personWorkDtos.add(personWorkDto);
            }
            month.setPersonWork(personWorkDtos);
        }
    }

    private BigDecimal calculateAvgSalaryForMonth(ProjectMonthDto month, UUID personId) {
        List<Salary> salaries = salaryRepo.getPersonActiveSalariesInMonth(month.getDate().getMonthValue(), month.getDate().getYear(), personId);
        return StatisticUtil.calculateAvgMonthSalary(salaries, month.getDate().getMonthValue(), month.getDate().getYear());
    }

}