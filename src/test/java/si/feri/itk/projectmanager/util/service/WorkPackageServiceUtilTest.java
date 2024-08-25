package si.feri.itk.projectmanager.util.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.request.workpackage.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;
import si.feri.itk.projectmanager.model.WorkPackage;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WorkPackageServiceUtilTest {

    @Test
    public void testValidateCreateWorkPackageRequest() {
        CreateWorkPackageRequest request = new CreateWorkPackageRequest();
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setProjectId(UUID.randomUUID());
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setTitle("Test title");
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setIsRelevant(true);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setAssignedPM(-1L);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setAssignedPM(1L);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setStartDate(LocalDate.now());
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setEndDate(LocalDate.now().minusDays(1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });

        request.setEndDate(LocalDate.now());
        Assertions.assertDoesNotThrow(() -> {
            WorkPackageServiceUtil.validateCreateWorkPackageRequest(request);
        });
    }

    @Test
    public void createWorkPackageTest() {
        CreateWorkPackageRequest request = new CreateWorkPackageRequest();
        request.setTitle("Test title");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setIsRelevant(true);
        request.setProjectId(UUID.randomUUID());

        UUID projectId = UUID.randomUUID();
        WorkPackage workPackage = WorkPackageServiceUtil.createWorkPackage(request, projectId);
        Assertions.assertEquals(request.getTitle(), workPackage.getTitle());
        Assertions.assertEquals(request.getStartDate(), workPackage.getStartDate());
        Assertions.assertEquals(request.getEndDate(), workPackage.getEndDate());
        Assertions.assertEquals(request.getIsRelevant(), workPackage.getIsRelevant());
        Assertions.assertEquals(projectId, workPackage.getProjectId());
    }
}
