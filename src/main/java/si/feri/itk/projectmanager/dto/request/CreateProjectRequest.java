package si.feri.itk.projectmanager.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateProjectRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}
