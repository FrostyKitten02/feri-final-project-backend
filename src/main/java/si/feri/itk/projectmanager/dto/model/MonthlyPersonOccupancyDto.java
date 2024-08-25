package si.feri.itk.projectmanager.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MonthlyPersonOccupancyDto {
    private LocalDate month;
    private UUID personId;
    private BigDecimal estimatedPm;
    private BigDecimal maxAvailability;
}
