package si.feri.itk.projectmanager.dto.request.workpackage;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UpdateWorkPackageRequest implements IDuration {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRelevant;
    private Long assignedPM;
}
