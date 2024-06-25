package si.feri.itk.projectmanager.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Duration implements IDuration {
    private LocalDate startDate;
    private LocalDate endDate;
}
