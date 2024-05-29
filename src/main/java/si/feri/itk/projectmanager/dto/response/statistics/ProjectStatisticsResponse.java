package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectStatisticsResponse {
    private List<WorkPackageWithStatisticsDto> workPackages;
    private List<ProjectMonthDto> months;
}
