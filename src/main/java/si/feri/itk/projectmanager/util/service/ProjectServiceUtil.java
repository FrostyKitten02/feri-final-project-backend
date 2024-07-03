package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.project.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.UpdateProjectRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.interfaces.IProjectBudgetRequest;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.ProjectBudgetSchema;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;
import si.feri.itk.projectmanager.util.DateUtil;
import si.feri.itk.projectmanager.util.ProjectBudgetUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ProjectServiceUtil {
    private ProjectServiceUtil() {}

    public static void validateCreateProjectRequest(CreateProjectRequest request) {
        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw new BadRequestException("Title is required");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new BadRequestException("End date is required");
        }

        DateUtil.validateDurationStrict(request);

        if (request.getProjectBudgetSchemaId() == null) {
            throw new BadRequestException("Project budget schema is required");
        }

        if (request.getStaffBudget() == null) {
            throw new BadRequestException("Staff budget is required");
        }

        if (request.getTravelBudget() == null) {
            throw new BadRequestException("Travel budget is required");
        }

        if (request.getEquipmentBudget() == null) {
            throw new BadRequestException("Equipment budget is required");
        }

        if (request.getSubcontractingBudget() == null) {
            throw new BadRequestException("Subcontract budget is required");
        }
    }
    
    public static void validateUpdateProjectRequest(UpdateProjectRequest request, UUID projectId) {
        DateUtil.validateDurationStrict(request);
        
        if (projectId == null) {
            throw new BadRequestException("Project id is required");
        }
    }

    public static Project createNewProject(CreateProjectRequest request, String userId, BigDecimal indirectBudget) {
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setOwnerId(userId);
        project.setStaffBudget(request.getStaffBudget());
        project.setTravelBudget(request.getTravelBudget());
        project.setEquipmentBudget(request.getEquipmentBudget());
        project.setSubcontractingBudget(request.getSubcontractingBudget());
        project.setIndirectBudget(indirectBudget);
        project.setProjectBudgetSchemaId(request.getProjectBudgetSchemaId());
        return project;
    }
    
    public static void updateProject(UpdateProjectRequest request,
                                     Project project,
                                     ProjectBudgetSchema schema,
                                     si.feri.itk.projectmanager.dto.common.Duration wpDuration,
                                     si.feri.itk.projectmanager.dto.common.Duration occupancyDuration
    ) {
        if (StringUtil.isNotNullOrEmpty(request.getTitle())) {
            project.setTitle(request.getTitle());
        }
        DateUtil.validateDurationStrict(request);

        LocalDate projectStartLimit = DateUtil.getFirstDate(wpDuration.getStartDate(), occupancyDuration.getStartDate());
        setProjectStartDate(project, request.getStartDate(), projectStartLimit);

        LocalDate projectFinishLimit = DateUtil.getLastDate(wpDuration.getEndDate(), occupancyDuration.getEndDate());
        setProjectEndDate(project, request.getEndDate(), projectFinishLimit);

        updateProjectBudget(request, project, schema);
    }

    private static void setProjectStartDate(Project project, LocalDate newStartDate, LocalDate projectMaxStartLimit) {
        if (newStartDate == null) {
            return;
        }

        if (projectMaxStartLimit == null) {
            project.setStartDate(newStartDate);
            return;
        }

        if (newStartDate.isAfter(projectMaxStartLimit)) {
            throw new BadRequestException("New start date overlaps with work package or occupancy");
        }

        project.setStartDate(newStartDate);
    }

    private static void setProjectEndDate(Project project, LocalDate newEndDate, LocalDate projectMinEndLimit) {
        if (newEndDate == null) {
            return;
        }

        if (projectMinEndLimit == null) {
            project.setEndDate(newEndDate);
            return;
        }

        if (newEndDate.isBefore(projectMinEndLimit)) {
            throw new BadRequestException("New end date overlaps with work package or occupancy");
        }

        project.setEndDate(newEndDate);
    }

    private static void updateProjectBudget(IProjectBudgetRequest request, Project project, ProjectBudgetSchema schema) {
        boolean isBudgetChanged = false;
        if (request.getProjectBudgetSchemaId() != null) {
            project.setProjectBudgetSchemaId(request.getProjectBudgetSchemaId());
            isBudgetChanged = true;
        }

        if (request.getStaffBudget() != null) {
            project.setStaffBudget(request.getStaffBudget());
            isBudgetChanged = true;
        }

        if (request.getTravelBudget() != null) {
            project.setTravelBudget(request.getTravelBudget());
            isBudgetChanged = true;
        }

        if (request.getEquipmentBudget() != null) {
            project.setEquipmentBudget(request.getEquipmentBudget());
            isBudgetChanged = true;
        }

        if (request.getSubcontractingBudget() != null) {
            project.setSubcontractingBudget(request.getSubcontractingBudget());
            isBudgetChanged = true;
        }

        if (isBudgetChanged) {
            project.setIndirectBudget(ProjectBudgetUtil.calculateIndirectBudget(project, schema));
        }
    }
    
    public static void validateAddPersonToProjectRequest(UUID projectId, AddPersonToProjectRequest request) {
        if (projectId == null) {
            throw new BadRequestException("Project id is required");
        }

        if (request.getPersonId() == null) {
            throw new BadRequestException("Person id is required");
        }
    }

    public static PersonOnProject createNewPersonOnProject(Project project, Person person) {
        PersonOnProject personOnProject = new PersonOnProject();
        personOnProject.setPersonId(person.getId());
        personOnProject.setProjectId(project.getId());
        return personOnProject;
    }
}
