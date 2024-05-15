package si.feri.itk.projectmanager.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePersonTypeRequest {
    private String name;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal researchAvailability;

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.00")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal educateAvailability;
}
