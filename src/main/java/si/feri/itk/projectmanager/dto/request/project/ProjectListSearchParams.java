package si.feri.itk.projectmanager.dto.request.project;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectListSearchParams {
    private String searchStr;
    private Boolean searchOnlyOwnedProjects;

    private LocalDate startDateFrom;
    private LocalDate startDateTo;

    private LocalDate endDateFrom;
    private LocalDate endDateTo;
}
