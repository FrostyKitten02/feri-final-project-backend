package si.feri.itk.projectmanager.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequest implements IDuration {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRelevant;
    private UUID workPackageId;
}
