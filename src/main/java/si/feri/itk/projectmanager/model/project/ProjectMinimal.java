package si.feri.itk.projectmanager.model.project;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import si.feri.itk.projectmanager.configuration.ModelConstants;
import si.feri.itk.projectmanager.model.BaseModel;

import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = ModelConstants.PROJECT_TABLE)
public class ProjectMinimal extends BaseModel {
    private String title;
    private String ownerId;
    private LocalDate startDate;
}
