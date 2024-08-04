package si.feri.itk.projectmanager.model.project;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.configuration.ModelConstants;
import si.feri.itk.projectmanager.interfaces.IProjectBudget;
import si.feri.itk.projectmanager.model.BaseModel;
import si.feri.itk.projectmanager.model.WorkPackage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = ModelConstants.PROJECT_TABLE)
public class Project extends BaseModel implements IProjectBudget {
    private String title;
    private String ownerId;//clerk id
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToMany(
            mappedBy = "projectId",
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE
    )
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
    private BigDecimal subcontractingBudget;
    @Column(precision = 10, scale = 2)
    private BigDecimal indirectBudget;
}
