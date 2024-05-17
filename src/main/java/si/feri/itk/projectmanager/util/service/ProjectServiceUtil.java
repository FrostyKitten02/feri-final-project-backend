package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;
import si.feri.itk.projectmanager.util.StringUtil;

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
    }

    public static Project createNewProject(CreateProjectRequest request, String userId) {
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setOwnerId(userId);
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
