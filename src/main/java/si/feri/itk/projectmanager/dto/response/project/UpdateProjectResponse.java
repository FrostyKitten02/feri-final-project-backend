package si.feri.itk.projectmanager.dto.response.project;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.ProjectDto;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProjectResponse {
    private ProjectDto project;
}
