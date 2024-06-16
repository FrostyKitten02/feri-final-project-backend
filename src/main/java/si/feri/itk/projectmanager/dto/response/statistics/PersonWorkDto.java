package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PersonWorkDto {
    private UUID personId;
    private UUID occupancyId;
    private BigDecimal totalWorkPm;
    private BigDecimal avgSalary;
}
