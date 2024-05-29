package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProjectMonthDto {
    //first day of the month
    private LocalDate date;
    private Integer monthNumber;
    //how many pm-s to burn down this month
    private BigDecimal pmBurnDownRate = BigDecimal.ZERO;
    public void addBurnDownRate(BigDecimal burnDownRate) {
        this.pmBurnDownRate = this.pmBurnDownRate.add(burnDownRate);
    }
}
