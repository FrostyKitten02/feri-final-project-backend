package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectMonthDto {
    //first day of the month
    private LocalDate date;
    private Integer monthNumber;
    //how many pm-s to burn down this month
    private BigDecimal pmBurnDownRate = BigDecimal.ZERO;

    private BigDecimal actualTotalWorkPm = BigDecimal.ZERO;
    private BigDecimal actualMonthSpending = BigDecimal.ZERO;

    private List<PersonWorkDto> personWork;

    public void addBurnDownRate(BigDecimal burnDownRate) {
        this.pmBurnDownRate = this.pmBurnDownRate.add(burnDownRate);
    }
    public void addWorkPm(BigDecimal workPm) {
        this.actualTotalWorkPm = this.actualTotalWorkPm.add(workPm);
    }

    public void addActualSpending(BigDecimal newSpending) {
        this.actualMonthSpending = this.actualMonthSpending.add(newSpending);
    }

}
