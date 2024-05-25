package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;
import si.feri.itk.projectmanager.util.StringUtil;

import java.math.BigDecimal;
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

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }

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

        if (request.getSubcontractBudget() == null) {
            throw new BadRequestException("Subcontract budget is required");
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
        project.setSubcontractBudget(request.getSubcontractBudget());
        project.setIndirectBudget(indirectBudget);
        return project;
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
