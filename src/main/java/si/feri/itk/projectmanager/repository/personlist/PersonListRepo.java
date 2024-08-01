package si.feri.itk.projectmanager.repository.personlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.PersonList;

import java.util.UUID;

@Repository
public interface PersonListRepo extends JpaRepository<PersonList, UUID>, CustomPersonListRepo {
}
