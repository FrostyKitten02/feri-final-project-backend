package si.feri.itk.projectmanager.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TaskDto {
    private UUID id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRelevant;
    private UUID workPackageId;
}
