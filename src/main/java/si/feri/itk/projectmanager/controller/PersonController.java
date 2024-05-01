package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.dto.response.GetPeopleResponse;
import si.feri.itk.projectmanager.dto.response.GetPersonResponse;
import si.feri.itk.projectmanager.service.PersonService;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    @GetMapping("/{personId}")
    public GetPersonResponse getPersonById(@PathVariable UUID personId, HttpServletRequest servletRequest) {
        PersonDto person = personService.getPersonById(personId);

        GetPersonResponse response = new GetPersonResponse();
        response.setPerson(person);
        return response;
    }

    @GetMapping("/people-on-project/{projectId}")
    public GetPeopleResponse getPeopleOnProjectByProjectId(@PathVariable UUID projectId, HttpServletRequest servletRequest) {
        List<PersonDto> people = personService.findPeopleOnProject(projectId);
        GetPeopleResponse response = new GetPeopleResponse();
        response.setPeople(people);
        response.setProjectId(projectId);
        return response;
    }
}
