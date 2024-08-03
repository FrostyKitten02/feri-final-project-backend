package si.feri.itk.projectmanager.util;

import lombok.extern.slf4j.Slf4j;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectMonthDto;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.dto.response.statistics.WorkPackageWithStatisticsDto;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.mapper.WorkPackageMapper;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.model.person.Salary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

            addWorkPackageBurnDownRateToProjectMonths(wp, projectMonthDtos, pmBurnDownPerWp);

            WorkPackageWithStatisticsDto wpWithStats = WorkPackageMapper.INSTANCE.toDtoWithStatistics(wp);
            wpWithStats.setPmBurnDownRate(pmBurnDownPerWp);
            wpWithStats.setPmBurnDownRatePerTask(pmBurnDownRatePerTask);

            wpWithStatsList.add(wpWithStats);
        }

        setMonthsStaffBudgetBurnDown(projectMonthDtos, project);

        ProjectStatisticsResponse projectStatisticsResponse = new ProjectStatisticsResponse();
        projectStatisticsResponse.setWorkPackages(wpWithStatsList);
        projectStatisticsResponse.setMonths(projectMonthDtos);
        return projectStatisticsResponse;
    }

    private static void setMonthsStaffBudgetBurnDown(List<ProjectMonthDto> projectMonthDtos, Project project) {
        BigDecimal totalPms = projectMonthDtos.stream().reduce(BigDecimal.ZERO, (acc, month) -> acc.add(month.getPmBurnDownRate()), BigDecimal::add);
        BigDecimal totalBudget = project.getStaffBudget();
        if (totalPms.compareTo(BigDecimal.ZERO) <= 0 || totalBudget.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Could not calculate staff budget burn down rates for project with id: {}, totalPms: {}, totalBudget: {}", project.getId(), totalPms.floatValue(), totalBudget.floatValue());
            return;
        }

        for (ProjectMonthDto month : projectMonthDtos) {
            BigDecimal monthBudgetPercentage = month.getPmBurnDownRate().divide(totalPms, RoundingMode.UP);
            BigDecimal monthBudget = totalBudget.multiply(monthBudgetPercentage);
            month.setStaffBudgetBurnDownRate(monthBudget);
        }
    }

    public static BigDecimal calculateAvgMonthSalary(List<Salary> salaries, int monthNumber, int yearNumber) {
        final int maxDays = DateUtil.calculateMonthMaxDay(monthNumber, yearNumber);
        int fromDay = 1;
        BigDecimal sum = BigDecimal.ZERO;
        for (Salary s : salaries) {
            if (s.getEndDate() != null && s.getEndDate().getMonthValue() != monthNumber) {
                throw new InternalServerException("Error creating salary statistics");
            }
            final int salaryEndDate;
            if (s.getEndDate() == null) {
                salaryEndDate = maxDays;
            } else {
                salaryEndDate = s.getEndDate().getDayOfMonth();
            }


            final int salaryDays = salaryEndDate - fromDay;
            fromDay = salaryEndDate + 1;
            sum = sum.add(s.getAmount().multiply(BigDecimal.valueOf(salaryDays)));
        }

        return sum.divide(BigDecimal.valueOf(maxDays), RoundingMode.CEILING);
    }

    private static void addWorkPackageBurnDownRateToProjectMonths(WorkPackage wp, List<ProjectMonthDto> projectMonthDtos, BigDecimal wpBurnDownRate) {
        int monthIndex = findIndexOfProjectMonthByMonth(wp.getStartDate(), projectMonthDtos);
        if (monthIndex == -1) {
            //if this happens, we will have some fun times debugging our app :)
            throw new InternalServerException("Task start date is not in project months range");
        }
        int wpMonthOffset = DateUtil.getMonthsBetweenDates(wp.getStartDate(), wp.getEndDate());

        for (int i = 0; i < wpMonthOffset ; i++) {
            ProjectMonthDto month = projectMonthDtos.get(monthIndex + i);
            month.addBurnDownRate(wpBurnDownRate);
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
