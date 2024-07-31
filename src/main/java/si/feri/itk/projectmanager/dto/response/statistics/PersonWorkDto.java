package si.feri.itk.projectmanager.dto.response.statistics;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PersonWorkDto {
    private PersonDto person;
    private UUID occupancyId;
    private BigDecimal totalWorkPm;
    private BigDecimal avgSalary;
}
