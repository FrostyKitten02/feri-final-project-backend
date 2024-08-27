package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import si.feri.itk.projectmanager.model.person.PersonType;

import java.time.LocalDate;
import java.util.UUID;

public interface PersonTypeRepo extends JpaRepository<PersonType, UUID> {

    @Query("SELECT pt FROM PersonType pt " +
            "WHERE pt.personId = :personId " +
            "AND pt.startDate <= :date " +
            "AND (pt.endDate IS NULL OR pt.endDate >= :date)")
    PersonType findByStartDateBeforeAndEndDateNullOrAfterAndPersonId(LocalDate date, UUID personId);

}
