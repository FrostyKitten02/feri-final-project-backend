package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.feri.itk.projectmanager.model.person.PersonHasType;

import java.util.UUID;

public interface PersonHasTypeRepo extends JpaRepository<PersonHasType, UUID> {
}
