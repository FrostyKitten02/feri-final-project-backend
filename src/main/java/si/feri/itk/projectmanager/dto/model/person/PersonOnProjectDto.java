package si.feri.itk.projectmanager.dto.model.person;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PersonOnProjectDto extends PersonDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal estimatedPm;
}
