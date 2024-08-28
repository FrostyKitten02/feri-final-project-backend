package si.feri.itk.projectmanager.model.person;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

@Entity
@Getter
@Setter
public class Person extends BaseModel {
    private String name;
    private String lastname;
    private String email;
    private String clerkId;
    private String profileImageUrl;
    private boolean isAdmin;
}
