package si.feri.itk.projectmanager.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import si.feri.itk.projectmanager.configuration.FileUploadConfig;
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
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsUnitDto;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.dto.request.project.ProjectSortInfoRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.PersonMapper;
import si.feri.itk.projectmanager.mapper.ProjectMapper;
import si.feri.itk.projectmanager.model.Occupancy;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.ProjectBudgetSchema;
import si.feri.itk.projectmanager.model.project.ProjectFile;
import si.feri.itk.projectmanager.model.project.ProjectList;
import si.feri.itk.projectmanager.model.ProjectStartingSoonEmailQueue;
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
import si.feri.itk.projectmanager.repository.ProjectFileRepo;
import si.feri.itk.projectmanager.repository.ProjectStartingSoonEmailQueueRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.repository.projectlist.ProjectListRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.SalaryRepo;
import si.feri.itk.projectmanager.util.DateUtil;
import si.feri.itk.projectmanager.util.ProjectBudgetUtil;
import si.feri.itk.projectmanager.util.ProjectStartingSoonUtil;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StatisticUtil;
import si.feri.itk.projectmanager.util.service.FileServiceUtil;
import si.feri.itk.projectmanager.util.service.ProjectServiceUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final ProjectFileRepo projectFileRepo;
    private final ProjectListRepo projectListRepo;
    private final FileUploadConfig fileUploadConfig;
    private final PersonOnProjectRepo personOnProjectRepo;
    private final ProjectBudgetSchemaRepo projectBudgetSchemaRepo;
    private final ProjectStartingSoonEmailQueueRepo projectStartingSoonEmailQueueRepo;

    @Transactional
    public UUID createProject(CreateProjectRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        ProjectServiceUtil.validateCreateProjectRequest(request);

        ProjectBudgetSchema schema = projectBudgetSchemaRepo.findById(request.getProjectBudgetSchemaId()).orElseThrow(()-> new ItemNotFoundException("Project budget schema not found"));
        BigDecimal indirectBudget = ProjectBudgetUtil.calculateIndirectBudget(request, schema);

        Project project = ProjectServiceUtil.createNewProject(request, userId, indirectBudget);
        Project savedProject = projectRepo.save(project);

        if (ProjectStartingSoonUtil.shouldCreateOrUpdateProjectStartingSoonEmailQueue(savedProject)) {
            ProjectStartingSoonEmailQueue email = ProjectStartingSoonUtil.createProjectStartingSoonEmailQueue(savedProject);
            projectStartingSoonEmailQueueRepo.save(email);
        }

        return savedProject.getId();
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
        projectStartingSoonEmailQueueRepo.deleteAllByProjectId(projectId);
        deleteProjectFiles(projectId);
        projectRepo.delete(project);
    }

    //TODO somehow handle if parent method doesnt complete so we dont delete files
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void deleteProjectFiles(UUID projectId) {
        List<ProjectFile> projectFiles = projectFileRepo.findAllByProjectId(projectId);
        for (ProjectFile projectFile : projectFiles) {
            boolean deleted = FileServiceUtil.deleteProjectFile(projectFile, fileUploadConfig.getRootUploadFolder());
            if (!deleted) {
                log.error("Failed to delete project file {}", projectFile.getStoredFilePath());
            }
            projectFileRepo.delete(projectFile);
        }
    }

    @Transactional
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

        LocalDate prevStartDate = project.getStartDate();
        ProjectServiceUtil.updateProject(request, project, schema, wpDuration, occupancyDuration);
        final Project savedProject = projectRepo.save(project);


        ProjectStartingSoonEmailQueue currentEmail = projectStartingSoonEmailQueueRepo.findByProjectId(projectId);
        if (ProjectStartingSoonUtil.shouldCreateOrUpdateProjectStartingSoonEmailQueue(savedProject)) {
            if (currentEmail != null) {
                ProjectStartingSoonUtil.updateProjectStartingSoonEmailQueue(currentEmail, savedProject, prevStartDate);
            } else {
                currentEmail = ProjectStartingSoonUtil.createProjectStartingSoonEmailQueue(savedProject);
            }
            projectStartingSoonEmailQueueRepo.save(currentEmail);
        } else {
            projectStartingSoonEmailQueueRepo.deleteAllByProjectId(projectId);
        }

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

    public void addPersonToProject(UUID projectId, @Validated AddPersonToProjectRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        ProjectServiceUtil.validateAddPersonToProjectRequest(projectId, request);
        DateUtil.validateDurationStrict(request);

        Project project = projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        DateUtil.validateChildDuration(request, project);

        PersonOnProject personOnProject = createPersonOnProject(project, request.getFrom(), request.getTo(), request.getEstimatedPm(), request.getPersonId());
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

    private PersonOnProject createPersonOnProject(Project project, LocalDate from, LocalDate to, BigDecimal estimatedPm, UUID personId) {
        Person person = personRepo.findById(personId).orElseThrow(() -> new ItemNotFoundException("Person not found"));

        Optional<PersonOnProject> personOnProjectDb = personOnProjectRepo.findFirstByProjectIdAndPersonId(project.getId(), person.getId());

        if (personOnProjectDb.isPresent()) {
            throw new BadRequestException("Person is already on project");
        }

        return ProjectServiceUtil.createNewPersonOnProject(project, person, from, to, estimatedPm);
    }

    public ListProjectResponse searchUsersProjects(PageInfoRequest pageInfoRequest, ProjectSortInfoRequest sortInfoRequest, ProjectListSearchParams searchParams, HttpServletRequest servletRequest) {
        SortInfo<?> sort = RequestUtil.getSortInfoFromRequest(sortInfoRequest);
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        Page<ProjectList> projectsPage = projectListRepo.searchUsersProjects(searchParams, userId, PageInfo.toPageRequest(pageInfoRequest, sort));
        return ListProjectResponse.fromPage(projectsPage);
    }

    public ProjectStatisticsResponse getProjectStatistics(UUID projectId, LocalDate from, Integer monthsPerUnit, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        Project project = projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        ProjectStatisticsResponse stats = StatisticUtil.calculateProjectStatistics(project, from, monthsPerUnit);

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
        for (ProjectStatisticsUnitDto unit : response.getUnits()) {
            ArrayList<PersonWorkDto> personWorkDtos = new ArrayList<>(people.size());
            for (Person p : people) {
                PersonWorkDto personWorkDto = new PersonWorkDto();
                personWorkDto.setPersonId(p.getId());
                List<Occupancy> occupancies = occupancyRepo.findAllBetweenMonthsAndPersonIdAndProjectId(unit.getStartDate(), unit.getEndDate(), p.getId(), projectId);

                if (occupancies.isEmpty()) {
                    continue;
                }

                if (unit.isMonthUnit()) {
                    if (occupancies.size() > 1) {
                        throw new RuntimeException("More than one occupancy for month unit");
                    }

                    personWorkDto.setOccupancyId(occupancies.getFirst().getId());
                }

                BigDecimal monthSalarySum = BigDecimal.ZERO;
                for (Occupancy o : occupancies) {
                    personWorkDto.addTotalWorkPm(o.getValue());
                    BigDecimal monthAvg = calculateAvgSalaryForMonth(o.getMonth(), p.getId());
                    BigDecimal monthSalary = monthAvg.multiply(o.getValue());
                    personWorkDto.addTotalSalary(monthSalary);

                    monthSalarySum = monthSalarySum.add(monthAvg);
                }
                final BigDecimal avgUnitSalary = monthSalarySum.divide(BigDecimal.valueOf(occupancies.size()), RoundingMode.CEILING);
                personWorkDto.setAvgSalary(avgUnitSalary);


                unit.addWorkPm(personWorkDto.getTotalWorkPm());
                unit.addActualSpending(personWorkDto.getTotalSalary());
                personWorkDtos.add(personWorkDto);
            }
            unit.setPersonWork(personWorkDtos);
        }
    }

    private BigDecimal calculateAvgSalaryForMonth(LocalDate startDate, UUID personId) {
        List<Salary> salaries = salaryRepo.getPersonActiveSalariesInMonth(startDate.getMonthValue(), startDate.getYear(), personId);
        return StatisticUtil.calculateAvgMonthSalary(salaries, startDate.getMonthValue(), startDate.getYear());
    }

}