package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepo extends JpaRepository<Person, UUID> {

    @Query("SELECT p " +
            "FROM Person AS p " +
            "WHERE p.id in " +
            "   (" +
            "   SELECT pop.person.id " +
            "   FROM PersonOnProject AS pop " +
            "   WHERE pop.projectId = :projectId" +
            "   )")
    List<Person> findAllByProjectId(UUID projectId);
    List<Person> findAllByClerkIdIn(List<String> personIds);
    @Query("SELECT p " +
            "FROM Person AS p " +
            "WHERE p.email = :email " +
            "AND p.clerkId IS NULL")
    List<Person> findAllByEmailAndClerkIdIsNull(String email);

    Optional<Person> findByClerkId(String clerkId);
}
