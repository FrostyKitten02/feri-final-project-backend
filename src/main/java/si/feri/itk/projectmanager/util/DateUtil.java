package si.feri.itk.projectmanager.util;

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

}
