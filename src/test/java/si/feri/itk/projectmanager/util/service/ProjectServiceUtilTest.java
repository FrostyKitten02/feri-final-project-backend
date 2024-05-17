package si.feri.itk.projectmanager.util.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.request.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceUtilTest {

    @Test
    public void testValidateCreateProjectRequest() {
        CreateProjectRequest request = new CreateProjectRequest();
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setTitle("Test title");
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setStartDate(LocalDate.now());
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setEndDate(LocalDate.now().minusDays(1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });


        request.setEndDate(LocalDate.now().plusDays(1));
        Assertions.assertDoesNotThrow(() -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });
    }

    @Test
    public void testCreateNewProject() {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setTitle("Test title");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        String userId = "testUserId";
        Project project = ProjectServiceUtil.createNewProject(request, userId);
        Assertions.assertEquals(request.getTitle(), project.getTitle());
        Assertions.assertEquals(request.getStartDate(), project.getStartDate());
        Assertions.assertEquals(request.getEndDate(), project.getEndDate());
        Assertions.assertEquals(userId, project.getOwnerId());
    }

    @Test
    public void testValidateAddPersonToProjectRequest() {
        AddPersonToProjectRequest request = new AddPersonToProjectRequest();
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateAddPersonToProjectRequest(null, request);
        });

        UUID projectId = UUID.randomUUID();
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateAddPersonToProjectRequest(projectId, request);
        });

        request.setPersonId(UUID.randomUUID());
        Assertions.assertDoesNotThrow(() -> {
            ProjectServiceUtil.validateAddPersonToProjectRequest(java.util.UUID.randomUUID(), new si.feri.itk.projectmanager.dto.request.AddPersonToProjectRequest());
        });
    }

    @Test
    public void testCreateNewPersonOnProject() {
        Project project = new Project();
        project.setId(UUID.randomUUID());
        Person person = new Person();
        PersonOnProject personOnProject = ProjectServiceUtil.createNewPersonOnProject(project, person);
        Assertions.assertEquals(project.getId(), personOnProject.getProjectId());
        Assertions.assertEquals(person.getId(), personOnProject.getPersonId());
    }
}
