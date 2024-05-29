package si.feri.itk.projectmanager.util;

import si.feri.itk.projectmanager.dto.response.statistics.ProjectMonthDto;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.dto.response.statistics.WorkPackageWithStatisticsDto;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.mapper.WorkPackageMapper;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticUtil {
    private StatisticUtil() {}
    private static final int PM_SCALE = 3;

    public static ProjectStatisticsResponse calculateProjectStatistics(Project project) {
        List<WorkPackage> wps = project.getWorkPackages();
        List<ProjectMonthDto> projectMonthDtos = createProjectMonths(project.getStartDate(), project.getEndDate());

        List<WorkPackageWithStatisticsDto> wpWithStatsList = new ArrayList<>();
        for (WorkPackage wp : wps) {
            if (wp.getIsRelevant() == null || !wp.getIsRelevant()) {
                continue;
            }
            final int wpMonths = DateUtil.getMonthsBetweenDates(wp.getStartDate(), wp.getEndDate());
            final BigDecimal pmBurnDownPerWp = calculateWorkPackagePmBurnDownRate(wp.getAssignedPM(), wpMonths);
            final BigDecimal pmBurnDownRatePerTask = calculateTaskBurnDownRate(wp.getTasks(), wp.getAssignedPM());

            addTasksBurnDownToProjectMonths(wp.getTasks(), projectMonthDtos, pmBurnDownRatePerTask);

            WorkPackageWithStatisticsDto wpWithStats = WorkPackageMapper.INSTANCE.toDtoWithStatistics(wp);
            wpWithStats.setPmBurnDownRate(pmBurnDownPerWp);
            wpWithStats.setPmBurnDownRatePerTask(pmBurnDownRatePerTask);

            wpWithStatsList.add(wpWithStats);
        }


        ProjectStatisticsResponse projectStatisticsResponse = new ProjectStatisticsResponse();
        projectStatisticsResponse.setWorkPackages(wpWithStatsList);
        projectStatisticsResponse.setMonths(projectMonthDtos);
        return projectStatisticsResponse;
    }

    private static void addTasksBurnDownToProjectMonths(List<Task> tasks, List<ProjectMonthDto> projectMonthDtos, BigDecimal pmBurnDownRatePerTask) {
        for (Task task : tasks) {
            int monthIndex = findIndexOfProjectMonthByMonth(task.getStartDate(), projectMonthDtos);
            if (monthIndex == -1) {
                //if this happens, we will have some fun times debugging our app :)
                throw new InternalServerException("Task start date is not in project months range");
            }
            int taskMonthsMaxOffset = DateUtil.getMonthsBetweenDates(task.getStartDate(), task.getEndDate());

            for (int i = 0; i < taskMonthsMaxOffset ; i++) {
                ProjectMonthDto month = projectMonthDtos.get(monthIndex + i);
                month.addBurnDownRate(pmBurnDownRatePerTask);
            }
        }
    }

    private static int findIndexOfProjectMonthByMonth(LocalDate date, List<ProjectMonthDto> months) {
        for (ProjectMonthDto month : months) {
            if (DateUtil.isSameMonthAndYear(month.getDate(), date)) {
                return months.indexOf(month);
            }
        }

        return -1;
    }


    public static List<ProjectMonthDto> createProjectMonths(LocalDate startDate, LocalDate endDate) {
        LocalDate firstDate = startDate.withDayOfMonth(1);
        List<ProjectMonthDto> projectMonthDtos = new ArrayList<>();
        int monthNumber = 1;
        while (!DateUtil.isMonthAfter(firstDate, endDate)) {
            ProjectMonthDto month = new ProjectMonthDto();
            month.setDate(firstDate);
            month.setMonthNumber(monthNumber++);
            projectMonthDtos.add(month);
            firstDate = firstDate.plusMonths(1);
        }

        return projectMonthDtos;
    }

    public static BigDecimal calculateTaskBurnDownRate(List<Task> tasks, long wpAssignedPm) {
        int taskMonthsSum = 0;
        for (Task task : tasks) {
            if (task.getIsRelevant() == null || !task.getIsRelevant()) {
                continue;
            }

            taskMonthsSum += DateUtil.getMonthsBetweenDates(task.getStartDate(), task.getEndDate());
        }

        if (taskMonthsSum == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(wpAssignedPm).setScale(PM_SCALE, RoundingMode.UNNECESSARY).divide(BigDecimal.valueOf(taskMonthsSum), RoundingMode.CEILING);
    }

    public static BigDecimal calculateWorkPackagePmBurnDownRate(WorkPackage workPackage) {
        if (workPackage.getAssignedPM() == null) {
            throw new BadRequestException("Assigned PM is not set for work package with id: " + workPackage.getId());
        }
        final long assignedPM = workPackage.getAssignedPM();
        final int wpMonths = DateUtil.getMonthsBetweenDates(workPackage.getStartDate(), workPackage.getEndDate());

        return calculateWorkPackagePmBurnDownRate(assignedPM, wpMonths);
    }

    public static BigDecimal calculateWorkPackagePmBurnDownRate(long assignedPM, int wpMonths) {
        final BigDecimal bdAssignedPM = BigDecimal.valueOf(assignedPM).setScale(PM_SCALE, RoundingMode.UNNECESSARY);
        final BigDecimal bdWpMonths = BigDecimal.valueOf(wpMonths).setScale(PM_SCALE, RoundingMode.UNNECESSARY);

        return bdAssignedPM.divide(bdWpMonths, RoundingMode.CEILING);
    }


}
