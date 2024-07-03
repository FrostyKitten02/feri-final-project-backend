package si.feri.itk.projectmanager.dto.request.occupancy;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateOccupancyRequest {
    private UUID occupancyId;
    @DecimalMax(value = "1.000")
    @DecimalMin(value = "0.001")
    private BigDecimal value;
}
