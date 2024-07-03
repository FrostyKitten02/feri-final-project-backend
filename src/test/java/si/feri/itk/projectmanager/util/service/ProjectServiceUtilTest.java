package si.feri.itk.projectmanager.util.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.request.project.AddPersonToProjectRequest;
import si.feri.itk.projectmanager.dto.request.project.CreateProjectRequest;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceUtilTest {

    @Test
    public void testValidateCreateProjectRequest() {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setTitle("Test title");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setProjectBudgetSchemaId(UUID.randomUUID());
        request.setStaffBudget(BigDecimal.valueOf(100));
        request.setTravelBudget(BigDecimal.valueOf(101));
        request.setEquipmentBudget(BigDecimal.valueOf(102));
        request.setSubcontractingBudget(BigDecimal.valueOf(103));

        Assertions.assertDoesNotThrow(() -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setTitle(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });


        request.setTitle("Title");
        request.setStartDate(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setStartDate(LocalDate.now());
        request.setEndDate(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });


        request.setEndDate(LocalDate.now().minusDays(1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setEndDate(LocalDate.now().plusDays(1));
        request.setEquipmentBudget(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });


        request.setEquipmentBudget(BigDecimal.TEN);
        request.setTravelBudget(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });

        request.setTravelBudget(BigDecimal.TEN);
        request.setStaffBudget(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });


        request.setStaffBudget(BigDecimal.TEN);
        request.setSubcontractingBudget(null);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            ProjectServiceUtil.validateCreateProjectRequest(request);
        });
    }

    @Test
    public void testCreateNewProject() {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setTitle("Test title");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setStaffBudget(BigDecimal.valueOf(100));
        request.setTravelBudget(BigDecimal.valueOf(101));
        request.setEquipmentBudget(BigDecimal.valueOf(102));
        request.setSubcontractingBudget(BigDecimal.valueOf(103));
        request.setProjectBudgetSchemaId(UUID.randomUUID());
        String userId = "testUserId";
        BigDecimal indirectBudget = BigDecimal.valueOf(1000);
        Project project = ProjectServiceUtil.createNewProject(request, userId, indirectBudget);
        Assertions.assertEquals(request.getTitle(), project.getTitle());
        Assertions.assertEquals(request.getStartDate(), project.getStartDate());
        Assertions.assertEquals(request.getEndDate(), project.getEndDate());
        Assertions.assertEquals(userId, project.getOwnerId());
        Assertions.assertEquals(request.getStaffBudget(), project.getStaffBudget());
        Assertions.assertEquals(request.getTravelBudget(), project.getTravelBudget());
        Assertions.assertEquals(request.getEquipmentBudget(), project.getEquipmentBudget());
        Assertions.assertEquals(request.getSubcontractingBudget(), project.getSubcontractingBudget());
        Assertions.assertEquals(indirectBudget, project.getIndirectBudget());
        Assertions.assertEquals(request.getProjectBudgetSchemaId(), project.getProjectBudgetSchemaId());
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
            ProjectServiceUtil.validateAddPersonToProjectRequest(java.util.UUID.randomUUID(), request);
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
