package si.feri.itk.projectmanager.dto.response.person;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDtoImpl;
import si.feri.itk.projectmanager.dto.model.SalaryDto;

@Getter
@Setter
public class GetPersonResponse {
    private PersonDtoImpl person;
    private SalaryDto currentSalary;
}
