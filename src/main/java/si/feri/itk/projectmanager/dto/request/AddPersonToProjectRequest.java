package si.feri.itk.projectmanager.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddPersonToProjectRequest {
    private UUID personId;
}
