package si.feri.itk.projectmanager.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ProjectStartingSoonEmailQueue extends BaseModel {
    private UUID projectId;
    private LocalDate sendAt;
    private int attempts;
    private boolean sent;
}
