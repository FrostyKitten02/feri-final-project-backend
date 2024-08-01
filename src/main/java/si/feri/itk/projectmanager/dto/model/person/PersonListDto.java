package si.feri.itk.projectmanager.dto.model.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonListDto {
    private UUID id;
    private String name;
    private String lastname;
    private String email;

    private BigDecimal salary;
    private BigDecimal availability;
}
