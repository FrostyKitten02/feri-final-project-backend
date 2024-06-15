package si.feri.itk.projectmanager.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SalaryDto {
    private UUID personId;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
