package si.feri.itk.projectmanager.model.person;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.View;
import si.feri.itk.projectmanager.model.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PersonOnProject extends BaseModel {
    private UUID projectId;
    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;
    private LocalDate fromDate;
    private LocalDate toDate;
    @Column(precision = 4, scale = 3)
    private BigDecimal estimatedPm;
}
