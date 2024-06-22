package si.feri.itk.projectmanager.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectListStatusResponse {
    private Long finishedProjects;
    private Long inProgressProjects;
    private Long scheduledProjects;
}
