package si.feri.itk.projectmanager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
public class DateUtilTest {
    @Test
    public void testGetMonthsBetweenDates() {
        LocalDate start = LocalDate.parse("2021-01-03");
        LocalDate finish = LocalDate.parse("2021-01-30");

        int between = DateUtil.getMonthsBetweenDates(start, finish);
        Assertions.assertEquals(1, between);

        start = LocalDate.parse("2021-01-03");
        finish = LocalDate.parse("2021-02-04");

        between = DateUtil.getMonthsBetweenDates(start, finish);
        Assertions.assertEquals(2, between);
    }


}
