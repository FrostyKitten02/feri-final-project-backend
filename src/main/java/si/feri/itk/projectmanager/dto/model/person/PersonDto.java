package si.feri.itk.projectmanager.dto.model.person;

import java.math.BigDecimal;
import java.util.UUID;

public interface PersonDto {
    UUID getId();
    String getName();
    String getLastname();
    String getEmail();
}
