package si.feri.itk.projectmanager.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {
    private String title;
    private String ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WorkPackageDto> workPackages;
}
