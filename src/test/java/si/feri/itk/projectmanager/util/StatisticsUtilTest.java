package si.feri.itk.projectmanager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectMonthDto;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class StatisticsUtilTest {

    @Test
    public void testCalculateWorkPackagePmBurnDownRatePerTaskFromWp() {
        WorkPackage wp = createWpWithTasks();
        BigDecimal pmRatePerTask = StatisticUtil.calculateWorkPackagePmBurnDownRate(wp);
        Assertions.assertEquals(BigDecimal.valueOf(8334, 3), pmRatePerTask);
    }

    @Test
    public void testCalculateWorkPackagePmBurnDownRatePerTask() {
        long assignedPM = 50;
        int wpMonths = 12;

        BigDecimal res = StatisticUtil.calculateWorkPackagePmBurnDownRate(assignedPM, wpMonths);
        Assertions.assertEquals(BigDecimal.valueOf(4167, 3), res);

        assignedPM = 12;
        wpMonths = 48;

        res = StatisticUtil.calculateWorkPackagePmBurnDownRate(assignedPM, wpMonths);
        Assertions.assertEquals(BigDecimal.valueOf(250, 3), res);
    }

    @Test
    public void testCalculateTaskBurnDownRate() {
        WorkPackage wp = createWpWithTasks2();
        BigDecimal res = StatisticUtil.calculateTaskBurnDownRate(wp.getTasks(), wp.getAssignedPM());

        Assertions.assertEquals(BigDecimal.valueOf(5556, 3), res);
    }

    @Test
    public void testCreateProjectStatistics() {
        Project project = new Project();
        WorkPackage wp = createWpWithTasks();
        WorkPackage wp2 = createWpWithTasks2();
        project.setTitle("Super title");
        project.setEndDate(wp.getEndDate());
        project.setStartDate(wp.getStartDate());
        project.setWorkPackages(List.of(wp, wp2));
        project.setStaffBudget(BigDecimal.valueOf(100000L));

        BigDecimal totalPM = BigDecimal.valueOf(wp.getAssignedPM() + wp2.getAssignedPM());
        //we allow 2% error!!
        BigDecimal totalPmMin = totalPM.multiply(BigDecimal.valueOf(98, 2));
        BigDecimal totalPmMax = totalPM.multiply(BigDecimal.valueOf(102, 2));

        ProjectStatisticsResponse stats = StatisticUtil.calculateProjectStatistics(project);
        BigDecimal sum = stats.getMonths().stream().map(ProjectMonthDto::getPmBurnDownRate).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        Assertions.assertTrue(sum.compareTo(totalPmMin) > 0);
        Assertions.assertTrue(sum.compareTo(totalPmMax) < 0);
    }

    private List<Task> createTasks() {
        LocalDate startDate = LocalDate.parse("2021-01-01");
        LocalDate endDate = LocalDate.parse("2021-01-25");
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = createTask(startDate, endDate);
            tasks.add(task);
            if (i % 3 == 0 && i != 0) {
                task.setStartDate(task.getStartDate().minusMonths(1));
                task.setEndDate(task.getEndDate().plusMonths(1));
            }
            startDate = startDate.plusMonths( 1);
            endDate = endDate.plusMonths(1);
        }
        return tasks;
    }

    private List<Task> createTasks2() {
        LocalDate startDate = LocalDate.parse("2021-01-01");
        LocalDate endDate = LocalDate.parse("2021-01-25");
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Task task = createTask(startDate, endDate);
            tasks.add(task);
            if (i % 3 == 0 && i != 0) {
                task.setStartDate(task.getStartDate().minusMonths(1));
                task.setEndDate(task.getEndDate().plusMonths(1));
            }
            startDate = startDate.plusMonths( 1);
            endDate = endDate.plusMonths(1);
        }
        return tasks;
    }

    private Task createTask(LocalDate startDate, LocalDate endDate) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setStartDate(startDate);
        task.setIsRelevant(true);
        task.setEndDate(endDate);
        return task;
    }

    private WorkPackage createWpWithTasks() {
        WorkPackage wp = new WorkPackage();
        wp.setAssignedPM(100L);
        wp.setStartDate(LocalDate.parse("2021-01-01"));
        wp.setEndDate(LocalDate.parse("2021-12-31"));
        wp.setTasks(createTasks());
        wp.setIsRelevant(true);
        return wp;
    }

    private WorkPackage createWpWithTasks2() {
        WorkPackage wp = new WorkPackage();
        wp.setAssignedPM(100L);
        wp.setStartDate(LocalDate.parse("2021-01-01"));
        wp.setEndDate(LocalDate.parse("2021-12-31"));
        wp.setTasks(createTasks2());
        wp.setIsRelevant(true);
        return wp;
    }
}
