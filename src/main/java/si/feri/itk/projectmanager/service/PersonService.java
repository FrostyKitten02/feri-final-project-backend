package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.PersonMapper;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.PersonOnProject;
import si.feri.itk.projectmanager.repository.PersonOnProjectRepo;
import si.feri.itk.projectmanager.repository.PersonRepo;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepo personRepo;
    public PersonDto getPersonById(UUID personId) {
        return personRepo.findById(personId).map(PersonMapper.INSTANCE::toDto).orElseThrow(() -> new ItemNotFoundException("Person not found"));
    }

    public List<PersonDto> findPeopleOnProject(UUID projectId) {
        List<Person> people = personRepo.findAllByProjectId(projectId);
        return people.stream().map(PersonMapper.INSTANCE::toDto).collect(Collectors.toList());
    }


}
