package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.persontype.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.dto.request.persontype.PersonTypeListSearchParams;
import si.feri.itk.projectmanager.dto.request.persontype.PersonTypeListSortInfoRequest;
import si.feri.itk.projectmanager.dto.response.persontype.ListPersonTypeResponse;
import si.feri.itk.projectmanager.exceptions.implementation.UnauthorizedException;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonType;
import si.feri.itk.projectmanager.model.person.PersonTypeList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.PersonTypeRepo;
import si.feri.itk.projectmanager.repository.persontypelist.PersonTypeListRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.PersonTypeServiceUtil;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonTypeService {
    private final PersonTypeRepo personTypeRepo;
    private final PersonRepo personRepo;
    private final PersonTypeListRepo personTypeListRepo;

    public UUID createPersonType(CreatePersonTypeRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        Person person = personRepo.findByClerkId(userId).orElseThrow(() -> new UnauthorizedException("User not found"));
        if (!person.isAdmin()) {
            throw new UnauthorizedException("Permission denied");
        }
        PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        PersonType personType = PersonTypeServiceUtil.createPersonType(request);
        return personTypeRepo.save(personType).getId();
    }

    public ListPersonTypeResponse searchPersonTypes(PageInfoRequest pageInfoRequest, PersonTypeListSortInfoRequest sortInfoRequest, PersonTypeListSearchParams searchParams) {
        SortInfo<?> sort = RequestUtil.getSortInfoFromRequest(sortInfoRequest);
        Page<PersonTypeList> typeListPage = personTypeListRepo.searchPersonTypes(searchParams, PageInfo.toPageRequest(pageInfoRequest, sort));
        return ListPersonTypeResponse.fromPage(typeListPage);
    }

}
