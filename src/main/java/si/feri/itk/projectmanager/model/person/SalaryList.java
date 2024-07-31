package si.feri.itk.projectmanager.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Immutable
public class SalaryList {
    @Id
    private UUID id;
    private UUID personId;
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
