package si.feri.itk.projectmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.feri.itk.projectmanager.model.person.PersonType;

import java.util.UUID;

public interface PersonTypeRepo extends JpaRepository<PersonType, UUID> {
}
