package si.feri.itk.projectmanager.repository.projectlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import si.feri.itk.projectmanager.dto.request.project.ProjectListSearchParams;
import si.feri.itk.projectmanager.model.project.ProjectList;

public interface CustomProjectListRepo {
    Page<ProjectList> searchUsersProjects(ProjectListSearchParams searchParams, String userId, Pageable pageable);
}
