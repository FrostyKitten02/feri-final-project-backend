package si.feri.itk.projectmanager.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateWorkPackageRequest implements IDuration {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRelevant;
    @Min(1)
    private Long assignedPM;
    private UUID projectId;
}
