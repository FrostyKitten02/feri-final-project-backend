package si.feri.itk.projectmanager.dto.request.task;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AddPersonToTaskRequest {
    @Deprecated
    @DecimalMax(value = "1.000")
    @DecimalMin(value = "0.001")
    private BigDecimal occupancy;
    private LocalDate startDate;
    private LocalDate endDate;
}
