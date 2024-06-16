package si.feri.itk.projectmanager.dto.response.occupancy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOccupancyResponse extends ResourceCreatedResponse {
    private List<OccupancyWarning> occupancyWarnings;
}
