package si.feri.itk.projectmanager.dto.response.project;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.ProjectFileDto;

import java.util.List;

@Getter
@Setter
public class ProjectFilesResponse {
    private List<ProjectFileDto> files;
}
