package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import si.feri.itk.projectmanager.model.MonthlyPersonOccupancy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MonthlyPersonOccupancyRepo extends JpaRepository<MonthlyPersonOccupancy, UUID> {
    @Query("SELECT mpo " +
            "FROM MonthlyPersonOccupancy mpo " +
            "WHERE mpo.month >= :fromMonth " +
            "AND mpo.month <= :toMonth " +
            "AND mpo.personId = :personId")
    List<MonthlyPersonOccupancy> findAllByMonthIsBetweenAndPersonIdEquals(LocalDate fromMonth, LocalDate toMonth, UUID personId);
}
