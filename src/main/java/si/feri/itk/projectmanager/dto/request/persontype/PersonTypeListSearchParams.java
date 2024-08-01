package si.feri.itk.projectmanager.dto.request.persontype;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PersonTypeListSearchParams {
    private UUID forUser;

    private LocalDate startDateFrom;
    private LocalDate startDateTo;

    private LocalDate endDateFrom;
    private LocalDate endDateTo;
}
