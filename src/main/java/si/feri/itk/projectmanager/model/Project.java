package si.feri.itk.projectmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.interfaces.IProjectBudget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Project extends BaseModel implements IProjectBudget {
    private String title;
    private String ownerId;//clerk id
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToMany(mappedBy = "projectId")
    private List<WorkPackage> workPackages;

    private UUID projectBudgetSchemaId;
    //budget
    @Column(precision = 10, scale = 2)
    private BigDecimal staffBudget;
    @Column(precision = 10, scale = 2)
    private BigDecimal travelBudget;
    @Column(precision = 10, scale = 2)
    private BigDecimal equipmentBudget;
    @Column(precision = 10, scale = 2)
    private BigDecimal subcontractBudget;
    @Column(precision = 10, scale = 2)
    private BigDecimal indirectBudget;
}
