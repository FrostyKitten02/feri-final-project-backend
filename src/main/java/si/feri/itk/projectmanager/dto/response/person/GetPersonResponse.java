package si.feri.itk.projectmanager.dto.response.person;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;
import si.feri.itk.projectmanager.dto.model.salary.SalaryDto;

@Getter
@Setter
public class GetPersonResponse {
    private PersonDto person;
    private SalaryDto currentSalary;
}
