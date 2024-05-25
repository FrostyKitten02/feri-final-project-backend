package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.ProjectBudgetSchema;

import java.util.UUID;

@Repository
public interface ProjectBudgetSchemaRepo extends JpaRepository<ProjectBudgetSchema, UUID> {
}
