package si.feri.itk.projectmanager.util;

import si.feri.itk.projectmanager.dto.common.IDuration;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtil {
    private DateUtil() {}

    //used for wp months calculations, maybe also for task dates calculations
    //counts number of months between two dates, including the months of each date, if they are both on same day of month but one month apart this will return 2
    //returns number of months this two dates span over
    public static int getMonthsBetweenDates(LocalDate startDate, LocalDate endDate) {
        int starMonth = startDate.getMonthValue();
        int endMonth = endDate.getMonthValue();

        int yearDiff = endDate.getYear() - startDate.getYear();

        return (yearDiff * 12 + endMonth) - starMonth + 1;
    }

    public static boolean isSameMonthAndYear(LocalDate startDate, LocalDate endDate) {
        return startDate.getMonthValue() == endDate.getMonthValue() && startDate.getYear() == endDate.getYear();
    }

    //true if startDate month is after endDate
    public static boolean isMonthAfter(LocalDate startDate, LocalDate endDate) {
        return startDate.getYear() > endDate.getYear() || (startDate.getYear() == endDate.getYear() && startDate.getMonthValue() > endDate.getMonthValue());
    }

    public static int calculateMonthMaxDay(LocalDate date) {
        return calculateMonthMaxDay(date.getMonthValue(), date.getYear());
    }

    public static int calculateMonthMaxDay(int month, int year) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static LocalDate getLastDate(LocalDate date1, LocalDate date2) {
        if (date2 == null) {
            return date1;
        }

        if (date1 == null) {
            return date2;
        }

        if (date1.isAfter(date2)) {
            return date1;
        }

        return date2;
    }

    public static LocalDate getFirstDate(LocalDate date1, LocalDate date2) {
        if (date1 == null) {
            return date2;
        }

        if (date2 == null) {
            return date1;
        }

        if (date1.isBefore(date2)) {
            return date1;
        }

        return date2;
    }


    /**
     *
     * @param duration time duration with start and end date
     * @throws BadRequestException if start date is after end date, start and end date can be on same day!
     */
    public static void validateDurationStrict(IDuration duration) {
        if (
                duration == null ||
                duration.getStartDate() == null ||
                duration.getEndDate() == null
        ) {
            return;
        }

        if (duration.getStartDate().isAfter(duration.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }
    }


    public static void validateChildDuration(IDuration childDuration, IDuration parentDuration) {
        if (childDuration.getEndDate() == null) {
            if (childDuration.getStartDate().isBefore(parentDuration.getStartDate())) {
                throw new BadRequestException("Start date is before parent start date");
            }
            return;
        }

        if (childDuration.getStartDate().isBefore(parentDuration.getStartDate())) {
            throw new BadRequestException("Start date is before parent start date");
        }

        if (childDuration.getEndDate().isAfter(parentDuration.getEndDate())) {
            throw new BadRequestException("Child end date is after parent end date");
        }
    }

}
