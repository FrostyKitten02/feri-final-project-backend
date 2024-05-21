package si.feri.itk.projectmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class PersonOnTask extends BaseModel {
    private UUID personId;
    private UUID taskId;
    @Column(precision = 4, scale = 3)
    private BigDecimal occupancy;
    private LocalDate startDate;
    private LocalDate endDate;
}
