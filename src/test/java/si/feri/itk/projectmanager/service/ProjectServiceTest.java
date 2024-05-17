package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import si.feri.itk.projectmanager.TestUtil;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.repository.PersonOnProjectRepo;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.ProjectListRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private  PersonRepo personRepo;
    @Mock
    private  ProjectRepo projectRepo;
    @Mock
    private ProjectListRepo projectListRepo;
    @Mock
    private PersonOnProjectRepo personOnProjectRepo;
    @InjectMocks
    private ProjectService projectService;


    private static String userId = "testUserId";
    private static HttpServletRequest servletRequest;

    @BeforeAll
    public static void setUserToRequest() {
        servletRequest = new MockHttpServletRequest();
        TestUtil.addUserIdToRequest(servletRequest, userId);
    }


    @Test
    public void testCreateProject() {
        Mockito.when(projectRepo.save(Mockito.any(Project.class))).thenAnswer(invocation -> {
            Project mockSaveProject = invocation.getArgument(0);
            mockSaveProject.ensureId();
            return mockSaveProject;
        });

        CreateProjectRequest request = new CreateProjectRequest();
        request.setTitle("Test title");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        UUID uuid = projectService.createProject(request, servletRequest);

        Assertions.assertNotNull(uuid);
    }
}
