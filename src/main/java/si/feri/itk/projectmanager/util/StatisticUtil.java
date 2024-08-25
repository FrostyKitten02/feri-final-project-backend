package si.feri.itk.projectmanager.util;

import lombok.extern.slf4j.Slf4j;
import si.feri.itk.projectmanager.dto.common.Duration;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsResponse;
import si.feri.itk.projectmanager.dto.response.statistics.ProjectStatisticsUnitDto;
import si.feri.itk.projectmanager.dto.response.statistics.WorkPackageWithStatisticsDto;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.mapper.WorkPackageMapper;
import si.feri.itk.projectmanager.model.Task;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.model.person.Salary;
import si.feri.itk.projectmanager.model.project.Project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class StatisticUtil {
    private StatisticUtil() {}
    private static final int PM_SCALE = 3;
    private static final int STATISTICS_DEFAULT_MONTHS_PER_UNIT = 1;

    public static ProjectStatisticsResponse calculateProjectStatistics(Project project, LocalDate from, Integer monthsNumber) {
        final LocalDate startDate = getStatisticsStartDate(project, from);
        final int monthsPerUnit = getMonthsPerUnit(monthsNumber);

        List<ProjectStatisticsUnitDto> projectStatisticsUnitDtos = createProjectUnits(startDate, project.getEndDate(), monthsPerUnit);
        List<WorkPackageWithStatisticsDto> wpWithStatsList = new ArrayList<>();
        List<WorkPackage> wps = project.getWorkPackages();
        for (WorkPackage wp : wps) {
            if (wp.getIsRelevant() == null || !wp.getIsRelevant()) {
                continue;
            }

            if (wp.getEndDate().isBefore(startDate)) {
                continue;
            }

            final int wpMonths = DateUtil.getMonthsBetweenDates(wp.getStartDate(), wp.getEndDate());
            final BigDecimal pmBurnDownPerWp = calculateWorkPackagePmBurnDownRate(wp.getAssignedPM(), wpMonths);
            final BigDecimal pmBurnDownRatePerTask = calculateTaskBurnDownRate(wp.getTasks(), wp.getAssignedPM());

            addWorkPackageBurnDownRateToProjectUnits(wp, projectStatisticsUnitDtos, pmBurnDownPerWp, monthsPerUnit, startDate);

            WorkPackageWithStatisticsDto wpWithStats = WorkPackageMapper.INSTANCE.toDtoWithStatistics(wp);
            wpWithStats.setPmBurnDownRate(pmBurnDownPerWp);
            wpWithStats.setPmBurnDownRatePerTask(pmBurnDownRatePerTask);

            wpWithStatsList.add(wpWithStats);
        }

        setUnitStaffBudgetBurnDownRate(projectStatisticsUnitDtos, project);

        ProjectStatisticsResponse projectStatisticsResponse = new ProjectStatisticsResponse();
        projectStatisticsResponse.setWorkPackages(wpWithStatsList);
        projectStatisticsResponse.setUnits(projectStatisticsUnitDtos);
        return projectStatisticsResponse;
    }

    private static LocalDate getStatisticsStartDate(Project project, LocalDate from) {
        if (from != null) {
            return from.withDayOfMonth(1);
        }

        return project.getStartDate();
    }

    private static int getMonthsPerUnit(Integer monthsNumber) {
        if (monthsNumber != null) {
            return monthsNumber;
        }

        return STATISTICS_DEFAULT_MONTHS_PER_UNIT;
    }

    private static void setUnitStaffBudgetBurnDownRate(List<ProjectStatisticsUnitDto> projectStatisticsUnitDtos, Project project) {
        BigDecimal totalPms = projectStatisticsUnitDtos.stream().reduce(BigDecimal.ZERO, (acc, unit) -> acc.add(unit.getPmBurnDownRate()), BigDecimal::add);
        BigDecimal totalBudget = project.getStaffBudget();
        if (totalPms.compareTo(BigDecimal.ZERO) <= 0 || totalBudget.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Could not calculate staff budget burn down rates for project with id: {}, totalPms: {}, totalBudget: {}", project.getId(), totalPms.floatValue(), totalBudget.floatValue());
            return;
        }

        for (ProjectStatisticsUnitDto unit : projectStatisticsUnitDtos) {
            BigDecimal unitBudgetPercentage = unit.getPmBurnDownRate().divide(totalPms, RoundingMode.UP);
            BigDecimal unitBudget = totalBudget.multiply(unitBudgetPercentage);
            unit.setStaffBudgetBurnDownRate(unitBudget);
        }
    }

    private static void addWorkPackageBurnDownRateToProjectUnits(WorkPackage wp, List<ProjectStatisticsUnitDto> projectStatisticsUnitDtos, BigDecimal wpBurnDownRate, int monthsPerUnit, LocalDate startDate) {
        final LocalDate wpStart = DateUtil.getLastDate(wp.getStartDate(), startDate);
        final int unitIndex = findProjectStatisticsUnitByDate(wpStart, projectStatisticsUnitDtos);
        if (unitIndex == -1) {
            //if this happens, we will have some fun times debugging our app :)
            throw new InternalServerException("No project statistics unit found for work-package");
        }


        final double wpUnitOffset = Math.ceil((double)DateUtil.getMonthsBetweenDates(wpStart, wp.getEndDate()) / monthsPerUnit);

        for (int i = 0; i < wpUnitOffset; i++) {
            ProjectStatisticsUnitDto unit = projectStatisticsUnitDtos.get(unitIndex + i);
            BigDecimal adjustedBurnDownRate = calculateActualPmBurnDownRateForUnit(wp, unit, wpBurnDownRate, monthsPerUnit);
            unit.addBurnDownRate(adjustedBurnDownRate);
        }
    }

    //UNITS MUST BE ORDERED BY START DATE ASC!
    private static int findProjectStatisticsUnitByDate(LocalDate date, List<ProjectStatisticsUnitDto> units) {
        for (ProjectStatisticsUnitDto unit : units) {
            if (DateUtil.isDateInDuration(date, unit)) {
                return units.indexOf(unit);
            }
        }

        return -1;
    }

    private static BigDecimal calculateActualPmBurnDownRateForUnit(WorkPackage wp, ProjectStatisticsUnitDto unit, BigDecimal monthlyWpBurnDownRate, int monthsPerUnit) {
        if (DateUtil.isDateInDuration(wp.getStartDate(), unit)) {
            final int months = DateUtil.getMonthsBetweenDates(wp.getStartDate(), unit.getEndDate());
            return monthlyWpBurnDownRate.multiply(BigDecimal.valueOf(months));
        } else if (DateUtil.isDateInDuration(wp.getEndDate(), unit)) {
            final int months = DateUtil.getMonthsBetweenDates(unit.getStartDate(), wp.getEndDate());
            return monthlyWpBurnDownRate.multiply(BigDecimal.valueOf(months));
        }

        return monthlyWpBurnDownRate.multiply(BigDecimal.valueOf(monthsPerUnit));
    }

    public static List<ProjectStatisticsUnitDto> createProjectUnits(LocalDate startDate, LocalDate endDate, int monthsPerUnit) {
        LocalDate firstDate = startDate.withDayOfMonth(1);
        LocalDate lastDate = firstDate.plusMonths(monthsPerUnit - 1);
        lastDate = lastDate.withDayOfMonth(lastDate.lengthOfMonth());

        List<ProjectStatisticsUnitDto> projectStatisticsUnitDtos = new ArrayList<>();
        int unitNumber = 1;
        while (!DateUtil.isMonthAfter(firstDate, endDate)) {
            ProjectStatisticsUnitDto unit = new ProjectStatisticsUnitDto();
            unit.setStartDate(firstDate);
            unit.setEndDate(lastDate);
            unit.setUnitNumber(unitNumber++);
            projectStatisticsUnitDtos.add(unit);
            firstDate = firstDate.plusMonths(monthsPerUnit);

            lastDate = lastDate.plusMonths(monthsPerUnit);
            lastDate = lastDate.withDayOfMonth(lastDate.lengthOfMonth());
        }

        return projectStatisticsUnitDtos;
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
}
