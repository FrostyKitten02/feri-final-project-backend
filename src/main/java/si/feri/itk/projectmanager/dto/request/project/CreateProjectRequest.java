package si.feri.itk.projectmanager.dto.request.project;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;
import si.feri.itk.projectmanager.interfaces.IProjectBudgetRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateProjectRequest implements IProjectBudgetRequest, IDuration {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    private UUID projectBudgetSchemaId;

    //budget
    @DecimalMax(value = "99999999.99")
    @DecimalMin(value = "0.00")
    private BigDecimal staffBudget;
    @DecimalMax(value = "99999999.99")
    @DecimalMin(value = "0.00")
    private BigDecimal travelBudget;
    @DecimalMax(value = "99999999.99")
    @DecimalMin(value = "0.00")
    private BigDecimal equipmentBudget;
    @DecimalMax(value = "99999999.99")
    @DecimalMin(value = "0.00")
    private BigDecimal subcontractingBudget;
}
