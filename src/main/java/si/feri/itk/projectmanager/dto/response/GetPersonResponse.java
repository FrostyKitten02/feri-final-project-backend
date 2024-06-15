package si.feri.itk.projectmanager.dto.response;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.dto.model.SalaryDto;

@Getter
@Setter
public class GetPersonResponse {
    private PersonDto person;
    private SalaryDto currentSalary;
}
