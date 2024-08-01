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
public class PersonTypeList {
    @Id
    private UUID id;
    private String name;

    @Column(precision = 3, scale = 2)
    private BigDecimal research;

    @Column(precision = 3, scale = 2)
    private BigDecimal educate;

    @Column(precision = 3, scale = 2)
    private BigDecimal maxAvailability;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID personId;
}
