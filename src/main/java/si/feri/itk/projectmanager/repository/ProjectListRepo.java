package si.feri.itk.projectmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.ProjectList;

import java.util.UUID;

@Repository
public interface ProjectListRepo extends JpaRepository<ProjectList, UUID> {
    Page<ProjectList> findAllByOwnerId(String ownerId, Pageable pageable);
}
