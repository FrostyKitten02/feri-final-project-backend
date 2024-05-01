package si.feri.itk.projectmanager.dto.response;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.PersonDto;

@Getter
@Setter
public class GetPersonResponse {
    private PersonDto person;
}
