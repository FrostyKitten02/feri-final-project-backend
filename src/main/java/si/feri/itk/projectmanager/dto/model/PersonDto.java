package si.feri.itk.projectmanager.dto.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PersonDto {
    private UUID id;
    private String name;
    private String lastname;
    private String email;
}
