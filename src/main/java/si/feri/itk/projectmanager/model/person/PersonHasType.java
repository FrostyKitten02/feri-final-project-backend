package si.feri.itk.projectmanager.model.person;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PersonHasType extends BaseModel {
    private UUID personId;
    private UUID personTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
}
