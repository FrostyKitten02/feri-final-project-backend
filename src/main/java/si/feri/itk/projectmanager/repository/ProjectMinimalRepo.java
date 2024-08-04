package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.project.ProjectMinimal;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMinimalRepo extends JpaRepository<ProjectMinimal, UUID> {
    @Query("select p " +
            "from ProjectMinimal as p " +
            "where p.id in :projectIds")
    List<ProjectMinimal> findAllByIdIn(List<UUID> projectIds);
}
