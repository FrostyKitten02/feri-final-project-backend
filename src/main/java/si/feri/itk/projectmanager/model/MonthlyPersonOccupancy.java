package si.feri.itk.projectmanager.model;

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
public class MonthlyPersonOccupancy {
    //this is randomly generated UUID on view, it is not constant!!!
    @Id
    private UUID id;
    private LocalDate month;
    private UUID personId;
    private BigDecimal estimatedPm;
    private BigDecimal maxAvailability;
}
