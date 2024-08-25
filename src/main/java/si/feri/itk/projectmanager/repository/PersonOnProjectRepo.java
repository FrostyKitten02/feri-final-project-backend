package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.PersonOnProject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonOnProjectRepo extends JpaRepository<PersonOnProject, UUID> {
    Optional<PersonOnProject> findFirstByProjectIdAndPersonId(UUID projectId, UUID personId);

    List<PersonOnProject> findAllByProjectId(UUID projectId);

    @Modifying
    @Query("DELETE FROM PersonOnProject p " +
            "WHERE p.projectId = :projectId ")
    void deleteAllByProjectId(UUID projectId);

    //this should only delete one row
    @Modifying
    @Query("DELETE FROM PersonOnProject p " +
            "WHERE p.person.id = :personId " +
            "AND p.projectId = :projectId ")
    void deleteAllByPersonIdAndProjectId(UUID personId, UUID projectId);
}
