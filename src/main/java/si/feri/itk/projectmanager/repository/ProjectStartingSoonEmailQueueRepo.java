package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.ProjectStartingSoonEmailQueue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectStartingSoonEmailQueueRepo extends JpaRepository<ProjectStartingSoonEmailQueue, UUID> {

    @Query("SELECT p " +
            "FROM ProjectStartingSoonEmailQueue p " +
            "WHERE p.sendAt <= :date " +
            "AND p.attempts < :attempts")
    List<ProjectStartingSoonEmailQueue> findAllBySendAtBeforeOrEqualAndAttemptsLessThan(LocalDate date, int attempts);

    ProjectStartingSoonEmailQueue findByProjectId(UUID projectId);

    @Modifying
    @Query("DELETE FROM ProjectStartingSoonEmailQueue p " +
            "WHERE p.projectId = :projectId")
    void deleteAllByProjectId(UUID projectId);

    @Modifying
    @Query("DELETE FROM ProjectStartingSoonEmailQueue p " +
            "WHERE p.sent = true")
    void deleteAllSent();
}
