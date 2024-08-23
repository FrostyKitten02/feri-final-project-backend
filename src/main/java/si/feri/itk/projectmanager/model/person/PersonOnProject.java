package si.feri.itk.projectmanager.model.person;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PersonOnProject extends BaseModel {
    private UUID projectId;
    private UUID personId;
    private LocalDate fromDate;
    private LocalDate toDate;
    @Column(precision = 4, scale = 3)
    private BigDecimal estimatedPm;
}
