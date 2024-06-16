package si.feri.itk.projectmanager.dto.response.occupancy;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OccupancyWarning {
    private LocalDate month;
    private Boolean avalibilityNotDefined;
    private BigDecimal maxOccupancy;
    private BigDecimal allocatedOccupancy;
}
