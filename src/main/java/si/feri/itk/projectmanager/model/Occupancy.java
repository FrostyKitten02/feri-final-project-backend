package si.feri.itk.projectmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Occupancy extends BaseModel {
    private UUID personId;
    private UUID projectId;

    private LocalDate month;
    @Column(precision = 4, scale = 3)
    private BigDecimal value;
}
