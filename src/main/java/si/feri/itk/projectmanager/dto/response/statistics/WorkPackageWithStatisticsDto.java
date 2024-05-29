package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.WorkPackageDto;

import java.math.BigDecimal;

@Getter
@Setter
public class WorkPackageWithStatisticsDto extends WorkPackageDto {
    private BigDecimal pmBurnDownRate;
    private BigDecimal pmBurnDownRatePerTask;
}
