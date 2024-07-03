package si.feri.itk.projectmanager.dto.request.task;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.common.IDuration;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateTaskRequest implements IDuration {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRelevant;
}
