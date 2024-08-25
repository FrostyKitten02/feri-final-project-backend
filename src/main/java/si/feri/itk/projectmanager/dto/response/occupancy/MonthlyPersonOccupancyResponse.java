package si.feri.itk.projectmanager.dto.response.occupancy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.MonthlyPersonOccupancyDto;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MonthlyPersonOccupancyResponse {
    private List<MonthlyPersonOccupancyDto> monthlyPersonOccupancies;
}
