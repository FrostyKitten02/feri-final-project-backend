package si.feri.itk.projectmanager.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;
import si.feri.itk.projectmanager.interfaces.IProjectBudget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectDto implements IProjectBudget, IDuration {
    private UUID id;
    private String title;
    private String ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WorkPackageDto> workPackages;
    private UUID projectBudgetSchemaId;

    //returned from project_list_view
    private Integer peopleCount;
    private Integer workPackageCount;

    //budget
    @Digits(integer = 8, fraction = 2)
    private BigDecimal staffBudget;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal travelBudget;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal equipmentBudget;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal subcontractingBudget;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal indirectBudget;
}
