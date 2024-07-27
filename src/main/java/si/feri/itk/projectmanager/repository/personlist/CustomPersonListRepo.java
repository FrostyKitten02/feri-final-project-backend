package si.feri.itk.projectmanager.repository.personlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import si.feri.itk.projectmanager.dto.request.person.PersonListSearchParams;
import si.feri.itk.projectmanager.model.person.PersonList;

public interface CustomPersonListRepo {
    Page<PersonList> searchPeople(PersonListSearchParams searchParams, Pageable pageable);
}
