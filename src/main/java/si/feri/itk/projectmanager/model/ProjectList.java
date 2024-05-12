package si.feri.itk.projectmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Immutable
public class ProjectList {
    @Id
    private UUID id;
    private String ownerId;
    private Instant createdAt;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}
