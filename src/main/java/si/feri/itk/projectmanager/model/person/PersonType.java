package si.feri.itk.projectmanager.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class PersonType extends BaseModel {
    private String name;

    @Column(precision = 3, scale = 2)
    private BigDecimal research;

    @Column(precision = 3, scale = 2)
    private BigDecimal educate;

    @Column(precision = 3, scale = 2)
    private BigDecimal maxAvailability;
}
