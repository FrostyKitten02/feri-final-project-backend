package si.feri.itk.projectmanager.dto.request.project;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddPersonToProjectRequest {
    private UUID personId;
}
