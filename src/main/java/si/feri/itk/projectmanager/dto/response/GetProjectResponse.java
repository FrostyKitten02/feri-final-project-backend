package si.feri.itk.projectmanager.dto.response;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.ProjectDto;

@Getter
@Setter
public class GetProjectResponse {
    private ProjectDto projectDto;
}
