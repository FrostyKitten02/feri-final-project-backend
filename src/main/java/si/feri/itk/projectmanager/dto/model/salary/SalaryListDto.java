package si.feri.itk.projectmanager.dto.model.salary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface SalaryListDto {
    UUID getPersonId();
    BigDecimal getAmount();
    LocalDate getStartDate();
    LocalDate getEndDate();
}
