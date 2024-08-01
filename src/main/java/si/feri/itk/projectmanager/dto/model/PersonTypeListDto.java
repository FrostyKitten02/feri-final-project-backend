package si.feri.itk.projectmanager.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonTypeListDto {
    private UUID id;
    private String name;

    private BigDecimal research;

    private BigDecimal educate;

    private BigDecimal maxAvailability;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID personId;
}
