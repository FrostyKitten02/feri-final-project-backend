package si.feri.itk.projectmanager.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateSalaryRequest {
    private UUID personId;
    @DecimalMax(value = "99999999.99")
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
