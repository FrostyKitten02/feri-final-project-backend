package si.feri.itk.projectmanager.dto.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ProjectFileDto {
    private UUID id;
    private Instant createdAt;
    private String contentType;
    private String originalFileName;
    private int fileSizeMB;
}
