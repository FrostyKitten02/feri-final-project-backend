package si.feri.itk.projectmanager.dto.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PersonTypeDto {
    private UUID id;
    private String name;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal research;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal educate;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal maxAvailability;
}
