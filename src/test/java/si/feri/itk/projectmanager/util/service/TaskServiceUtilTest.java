package si.feri.itk.projectmanager.util.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.request.task.AddPersonToTaskRequest;
import si.feri.itk.projectmanager.dto.request.task.CreateTaskRequest;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;
import si.feri.itk.projectmanager.model.PersonOnTask;
import si.feri.itk.projectmanager.model.Task;

import java.math.BigDecimal;
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


    @Test
    public void validateAddPersonToTaskTest() {
        UUID taskId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        AddPersonToTaskRequest request = new AddPersonToTaskRequest();
        request.setOccupancy(BigDecimal.valueOf(5, 1));
        request.setStartDate(LocalDate.now());

        Assertions.assertDoesNotThrow(() -> {
            TaskServiceUtil.validateAddPersonToTask(taskId, personId, request);
        });

        request.setEndDate(LocalDate.now().minusDays(1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateAddPersonToTask(taskId, personId, request);
        });

        request.setEndDate(LocalDate.now());
        request.setOccupancy(BigDecimal.valueOf(0));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateAddPersonToTask(taskId, personId, request);
        });

        request.setOccupancy(BigDecimal.valueOf(2));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            TaskServiceUtil.validateAddPersonToTask(taskId, personId, request);
        });

        request.setOccupancy(BigDecimal.valueOf(1));
        Assertions.assertDoesNotThrow(() -> {
            TaskServiceUtil.validateAddPersonToTask(taskId, personId, request);
        });
    }

    @Test
    public void createNewPersonOnTaskTest() {
        UUID taskId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        AddPersonToTaskRequest request = new AddPersonToTaskRequest();
        request.setOccupancy(BigDecimal.valueOf(1));
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));

        PersonOnTask personOnTask = TaskServiceUtil.createNewPersonOnTask(taskId, personId, request, null);
        Assertions.assertEquals(taskId, personOnTask.getTaskId());
        Assertions.assertEquals(personId, personOnTask.getPersonId());
        Assertions.assertEquals(request.getOccupancy(), personOnTask.getOccupancy());
        Assertions.assertEquals(request.getStartDate(), personOnTask.getStartDate());
        Assertions.assertEquals(request.getEndDate(), personOnTask.getEndDate());
    }

    @Test
    public void validatePersonOnTaskBoundsTest() {
        Task task = new Task();
        task.setStartDate(LocalDate.now());
        task.setEndDate(LocalDate.now().plusDays(1));

        PersonOnTask personOnTask = new PersonOnTask();
        personOnTask.setStartDate(LocalDate.now());
        personOnTask.setEndDate(LocalDate.now().plusDays(1));

        Assertions.assertDoesNotThrow(
                ()->TaskServiceUtil.validatePersonOnTaskBounds(personOnTask, task)
        );

        personOnTask.setEndDate(LocalDate.now());
        Assertions.assertDoesNotThrow(
                ()->TaskServiceUtil.validatePersonOnTaskBounds(personOnTask, task)
        );

        personOnTask.setStartDate(LocalDate.now().minusDays(1));
        Assertions.assertThrows(CustomRuntimeException.class ,
                ()->TaskServiceUtil.validatePersonOnTaskBounds(personOnTask, task)
        );
    }

}
