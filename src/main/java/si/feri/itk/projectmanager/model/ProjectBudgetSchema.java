package si.feri.itk.projectmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ProjectBudgetSchema extends BaseModel {
    private String name;
    @Column(precision = 3, scale = 2)
    private BigDecimal sofinancing;
    @Column(precision = 3, scale = 2)
    private BigDecimal indirectBudget;
}
