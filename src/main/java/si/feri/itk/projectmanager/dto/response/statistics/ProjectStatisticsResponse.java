package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ProjectStatisticsResponse {
    private List<WorkPackageWithStatisticsDto> workPackages;
    private List<ProjectMonthDto> months;
    private Map<UUID, PersonDto> people;
}
