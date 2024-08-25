package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PersonWorkDto {
    private UUID personId;
    private UUID occupancyId;
    private BigDecimal totalWorkPm = BigDecimal.ZERO;
    //this is avg salary per month
    private BigDecimal avgSalary;
    //this is total amount paid to person
    private BigDecimal totalSalary = BigDecimal.ZERO;
    public void addTotalWorkPm(BigDecimal workPm) {
        this.totalWorkPm = this.totalWorkPm.add(workPm);
    }
    public void addTotalSalary(BigDecimal salary) {
        this.totalSalary = this.totalSalary.add(salary);
    }
}
