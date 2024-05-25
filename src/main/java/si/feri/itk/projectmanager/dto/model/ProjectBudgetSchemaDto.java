package si.feri.itk.projectmanager.dto.model;

import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProjectBudgetSchemaDto {
    private String name;
    @Digits(integer = 1, fraction = 2)
    private BigDecimal sofinancing;
    @Digits(integer = 1, fraction = 2)
    private BigDecimal indirectBudget;
}
