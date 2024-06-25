package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.Task;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {
    @Query("SELECT MIN(t.startDate) " +
            "FROM Task t " +
            "WHERE t.workPackageId = :workPackageId")
    Optional<LocalDate> findEarliestDateByWorkPackageId(UUID workPackageId);

    @Query("SELECT MAX(t.endDate) " +
            "FROM Task t " +
            "WHERE t.workPackageId = :workPackageId")
    Optional<LocalDate> findLatestDateByWorkPackageId(UUID workPackageId);
}
