package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.WorkPackage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkPackageRepo extends JpaRepository<WorkPackage, UUID> {
    @Query("SELECT MAX(wp.endDate) " +
            "FROM WorkPackage wp " +
            "WHERE wp.projectId = :projectId")
    Optional<LocalDate> findLastWorkPackageEndDateByProjectId(UUID projectId);


    @Query("SELECT MIN(wp.startDate) " +
            "FROM WorkPackage wp " +
            "WHERE wp.projectId = :projectId")
    Optional<LocalDate> findFirstWorkPackageStartDateByProjectId(UUID projectId);

}
