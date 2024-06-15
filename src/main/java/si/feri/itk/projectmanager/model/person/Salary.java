package si.feri.itk.projectmanager.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Salary extends BaseModel {
    private UUID personId;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
