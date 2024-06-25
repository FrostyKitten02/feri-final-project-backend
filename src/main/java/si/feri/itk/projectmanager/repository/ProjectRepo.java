package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.Project;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepo extends JpaRepository<Project, UUID> {
    @Query("SELECT p FROM Project p WHERE p.id = :projectId AND p.ownerId = :ownerId")
    Optional<Project> findByIdAndOwnerId(@Param("projectId") UUID projectId, @Param("ownerId") String ownerId);


    @Query("select p from Project as p " +
            "where p.id = (" +
            "   select wp.projectId " +
            "   from WorkPackage as wp " +
            "   where wp.id = (" +
            "       select tsk.workPackageId " +
            "       from Task as tsk " +
            "       where tsk.id = :taskId " +
            "   ) " +
            " )" +
            "AND p.ownerId = :ownerId")
    Optional<Project> findProjectByTaskIdAndOwnerId(UUID taskId, String ownerId);

    @Query("select p from Project as p " +
            "where p.id = (" +
            "   select wp.projectId " +
            "   from WorkPackage as wp " +
            "   where wp.id = :workPackageId " +
            "   )" +
            "AND p.ownerId = :ownerId")
    Optional<Project> findProjectByWorkPackageIdAndOwnerId(UUID workPackageId, String ownerId);
}
