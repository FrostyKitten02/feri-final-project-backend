package si.feri.itk.projectmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

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
