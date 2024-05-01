package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.Person;

import java.util.UUID;

@Repository
public interface PersonRepo extends JpaRepository<Person, UUID> {
}
