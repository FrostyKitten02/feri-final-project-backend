package si.feri.itk.projectmanager.util.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.request.CreateTaskRequest;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;
import si.feri.itk.projectmanager.model.Task;

import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUtilTest {
    private TaskServiceUtilTest() {}

    @Test
    public void testValidateCreateTaskRequest() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setWorkPackageId(UUID.randomUUID());
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateCreateTaskRequest(request);
        });

        request.setTitle("Test title");
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateCreateTaskRequest(request);
        });

        request.setIsRelevant(true);
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateCreateTaskRequest(request);
        });

        request.setStartDate(LocalDate.now());
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateCreateTaskRequest(request);
        });

        request.setEndDate(LocalDate.now().minusDays(1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateCreateTaskRequest(request);
        });

        request.setEndDate(LocalDate.now());
        Assertions.assertDoesNotThrow(() -> {
            TaskServiceUtil.validateCreateTaskRequest(request);
        });
    }


    @Test
    public void createTaskTest() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test title");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setWorkPackageId(UUID.randomUUID());
        request.setIsRelevant(true);

        Task task = TaskServiceUtil.createNewTask(request);
        Assertions.assertEquals(request.getTitle(), task.getTitle());
        Assertions.assertEquals(request.getStartDate(), task.getStartDate());
        Assertions.assertEquals(request.getEndDate(), task.getEndDate());
        Assertions.assertEquals(request.getWorkPackageId(), task.getWorkPackageId());
        Assertions.assertEquals(request.getIsRelevant(), task.getIsRelevant());
    }
}
