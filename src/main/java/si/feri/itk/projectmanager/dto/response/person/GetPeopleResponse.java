package si.feri.itk.projectmanager.dto.response.person;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;
import si.feri.itk.projectmanager.dto.model.person.PersonOnProjectDto;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetPeopleResponse {
    private List<PersonOnProjectDto> people;
    private UUID projectId;
}
