package si.feri.itk.projectmanager.model.person;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

import java.util.UUID;

@Entity
@Getter
@Setter
public class PersonOnProject extends BaseModel {
    private UUID projectId;
    private UUID personId;
}
