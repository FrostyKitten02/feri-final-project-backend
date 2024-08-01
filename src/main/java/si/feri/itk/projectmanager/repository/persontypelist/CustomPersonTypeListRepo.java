package si.feri.itk.projectmanager.repository.persontypelist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import si.feri.itk.projectmanager.dto.request.persontype.PersonTypeListSearchParams;
import si.feri.itk.projectmanager.model.person.PersonTypeList;

public interface CustomPersonTypeListRepo {
    Page<PersonTypeList> searchPersonTypes(PersonTypeListSearchParams searchParams, Pageable pageable);
}
