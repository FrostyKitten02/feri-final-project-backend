package si.feri.itk.projectmanager.repository.persontypelist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.PersonTypeList;

import java.util.UUID;

@Repository
public interface PersonTypeListRepo extends JpaRepository<PersonTypeList, UUID>, CustomPersonTypeListRepo {
}
