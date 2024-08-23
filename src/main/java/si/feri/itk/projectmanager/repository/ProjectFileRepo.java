package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.feri.itk.projectmanager.model.project.ProjectFile;

import java.util.UUID;

public interface ProjectFileRepo extends JpaRepository<ProjectFile, UUID> {
}
