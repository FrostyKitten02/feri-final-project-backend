package si.feri.itk.projectmanager.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectBudgetSchemaDto {
    private UUID id;
    private String name;
    @Digits(integer = 1, fraction = 2)
    private BigDecimal sofinancing;
    @Digits(integer = 1, fraction = 2)
    private BigDecimal indirectBudget;
}
