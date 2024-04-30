package si.feri.itk.projectmanager.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class Person extends BaseModel {
    private String name;
    private String lastname;
    private String email;
    private String clerkId;
}
