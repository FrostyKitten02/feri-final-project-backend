package si.feri.itk.projectmanager.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class ProjectDto {
    private String title;
    private String ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WorkPackageDto> workPackages;
}
