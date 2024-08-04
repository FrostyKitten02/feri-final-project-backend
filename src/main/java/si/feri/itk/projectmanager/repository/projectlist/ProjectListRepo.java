package si.feri.itk.projectmanager.repository.projectlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.project.ProjectList;

import java.util.UUID;

@Repository
public interface ProjectListRepo extends JpaRepository<ProjectList, UUID>, CustomProjectListRepo {

}
