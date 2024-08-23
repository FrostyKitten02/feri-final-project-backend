package si.feri.itk.projectmanager.model.project;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.BaseModel;

import java.util.UUID;


@Entity
@Getter
@Setter
public class ProjectFile extends BaseModel {
    private UUID projectId;
    private String originalFileName;
    private String storedFilePath;
    @Column(name = "size_MB")
    private int fileSizeMB;
}
