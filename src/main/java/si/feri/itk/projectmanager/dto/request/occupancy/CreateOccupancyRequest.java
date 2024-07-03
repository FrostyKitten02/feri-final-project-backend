package si.feri.itk.projectmanager.dto.request.occupancy;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateOccupancyRequest implements IDuration {
    private UUID personId;
    private UUID projectId;

    private LocalDate fromMonth;
    private LocalDate toMonth;

    @DecimalMax(value = "1.000")
    @DecimalMin(value = "0.001")
    private BigDecimal value;

    public LocalDate getStartDate() {
        return fromMonth;
    }

    public LocalDate getEndDate() {
        return toMonth;
    }
}
