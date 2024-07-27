package si.feri.itk.projectmanager.dto.response.person;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDtoImpl;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetPeopleResponse {
    private List<PersonDtoImpl> people;
    private UUID projectId;
}
