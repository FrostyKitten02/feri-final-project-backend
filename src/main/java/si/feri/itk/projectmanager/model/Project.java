package si.feri.itk.projectmanager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.person.Person;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Project extends BaseModel {
    private String title;
    private String ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToMany(mappedBy = "projectId")
    private List<WorkPackage> workPackages;
}
