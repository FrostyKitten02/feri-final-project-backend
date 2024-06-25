package si.feri.itk.projectmanager.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Duration implements IDuration {

    public Duration() {}
    public Duration(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


    private LocalDate startDate;
    private LocalDate endDate;
}
