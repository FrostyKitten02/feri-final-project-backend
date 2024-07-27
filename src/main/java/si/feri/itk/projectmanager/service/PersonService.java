package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.model.person.PersonDtoImpl;
import si.feri.itk.projectmanager.dto.request.person.PersonListSearchParams;
import si.feri.itk.projectmanager.dto.request.person.PersonSortInfoRequest;
import si.feri.itk.projectmanager.dto.response.person.ListPersonResponse;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.PersonMapper;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.PersonSortInfo;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.personlist.PersonListRepo;
import si.feri.itk.projectmanager.util.RequestUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepo personRepo;
    private final ProjectRepo projectRepo;
    private final PersonListRepo personListRepo;
    public PersonDtoImpl getPersonById(UUID personId) {
        return personRepo.findById(personId).map(PersonMapper.INSTANCE::toDto).orElseThrow(() -> new ItemNotFoundException("Person not found"));
    }

    public List<PersonDtoImpl> findPeopleOnProject(UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        List<Person> people = personRepo.findAllByProjectId(projectId);
        return people.stream().map(PersonMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public List<PersonDtoImpl> getAllPeople() {
        return personRepo.findAll().stream().map(PersonMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public ListPersonResponse searchPeople(PageInfoRequest pageInfoRequest, PersonSortInfoRequest sortInfoRequest, PersonListSearchParams searchParams) {
        SortInfo<?> sort = RequestUtil.getSortInfoFromRequest(sortInfoRequest);
        Page<PersonList> peoplePage = personListRepo.searchPeople(searchParams, PageInfo.toPageRequest(pageInfoRequest, sort));
        return ListPersonResponse.fromPage(peoplePage);
    }

}
