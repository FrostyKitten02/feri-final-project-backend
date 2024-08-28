package si.feri.itk.projectmanager.model.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Immutable
public class PersonList {
    @Id
    private UUID id;
    private String name;
    private String lastname;
    private String email;
    private String profileImageUrl;

    //list extras
    private BigDecimal salary;
    private BigDecimal availability;
}
