package si.feri.itk.projectmanager.dto.request.person;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonListSearchParams {
    private String searchStr;
}
