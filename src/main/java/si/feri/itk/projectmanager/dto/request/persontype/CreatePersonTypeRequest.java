package si.feri.itk.projectmanager.dto.request.persontype;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreatePersonTypeRequest implements IDuration {
    private String name;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal research;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal educate;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID personId;
}
