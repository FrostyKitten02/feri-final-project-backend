package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.Occupancy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OccupancyRepo extends JpaRepository<Occupancy, UUID> {

    @Modifying
    @Query("DELETE FROM Occupancy o " +
            "WHERE o.month IN :localDates " +
            "AND o.projectId = :projectId " +
            "AND o.personId = :personId")
    void deleteAllByMonthInAndProjectIdAndPersonId(List<LocalDate> localDates, UUID projectId, UUID personId);


    @Query("SELECT SUM(o.value) FROM Occupancy o " +
            "WHERE o.month = :month " +
            "AND o.personId = :personId")
    BigDecimal sumAllByMonthAndPersonId(LocalDate month, UUID personId);

}
