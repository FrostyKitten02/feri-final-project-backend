package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.PersonMapper;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.util.RequestUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepo personRepo;
    private final ProjectRepo projectRepo;
    public PersonDto getPersonById(UUID personId) {
        return personRepo.findById(personId).map(PersonMapper.INSTANCE::toDto).orElseThrow(() -> new ItemNotFoundException("Person not found"));
    }

    public List<PersonDto> findPeopleOnProject(UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));
        List<Person> people = personRepo.findAllByProjectId(projectId);
        return people.stream().map(PersonMapper.INSTANCE::toDto).collect(Collectors.toList());
    }


}
