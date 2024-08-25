package si.feri.itk.projectmanager.dto.response.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectStatisticsUnitDto implements IDuration {
    //first day of the month
    private LocalDate startDate;
    //first day of the month
    private LocalDate endDate;

    private Integer unitNumber;
    //how many pm-s to burn down this unit (example: in a month)
    private BigDecimal pmBurnDownRate = BigDecimal.ZERO;
    //how much staff budget we can spend this unit (example: this month) based on pmBurnDownRate
    private BigDecimal staffBudgetBurnDownRate = BigDecimal.ZERO;

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

    @JsonIgnore
    public boolean isMonthUnit() {
        return startDate.equals(endDate);
    }

}
