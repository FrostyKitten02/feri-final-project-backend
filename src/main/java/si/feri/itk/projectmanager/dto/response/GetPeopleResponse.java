package si.feri.itk.projectmanager.dto.response;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.PersonDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetPeopleResponse {
    private List<PersonDto> people;
    private UUID projectId;
}
