package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.Salary;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalaryRepo extends JpaRepository<Salary, UUID> {
    @Query("SELECT s " +
            "FROM Salary s " +
            "WHERE s.personId = :personId " +
            "AND s.startDate <= :startDate " +
            "ORDER BY s.startDate DESC " +
            "LIMIT 1")
    Optional<Salary> findLatestByPersonIdAndStartDateIsBeforeOrEqual(UUID personId, LocalDate startDate);

    @Query("SELECT s " +
            "FROM Salary s " +
            "WHERE s.personId = :personId " +
            "ORDER BY s.startDate DESC " +
            "LIMIT 1")
    Optional<Salary> findLastByPersonId(UUID personId);
}
