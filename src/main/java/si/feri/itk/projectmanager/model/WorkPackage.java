package si.feri.itk.projectmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class WorkPackage extends BaseModel {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRelevant;
    private UUID projectId;
    @OneToMany(mappedBy = "workPackageId")
    private List<Task> tasks;
}
