package si.feri.itk.projectmanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResourceCreatedResponse {
    private UUID id;
    private List<UUID> ids;
}
