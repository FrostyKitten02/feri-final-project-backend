package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.Salary;

import java.time.LocalDate;
import java.util.List;
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


    @Query("SELECT s " +
            "FROM Salary s " +
            "WHERE (" +
            "   (" +
            "       month(s.endDate) = :month " +
            "       AND year(s.endDate) = :year" +
            "   ) " +
            "   OR s.endDate IS NULL" +
            ") " +
            "AND s.personId = :personId " +
            "ORDER BY s.endDate asc" )
    List<Salary> getPersonActiveSalariesInMonth(int month, int year, UUID personId);
}
