package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.model.PersonDto;
import si.feri.itk.projectmanager.dto.model.SalaryDto;
import si.feri.itk.projectmanager.dto.response.GetPersonResponse;
import si.feri.itk.projectmanager.service.PersonService;
import si.feri.itk.projectmanager.service.SalaryService;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final SalaryService salaryService;
    @GetMapping("/{personId}")
    public GetPersonResponse getPersonById(@PathVariable UUID personId, HttpServletRequest servletRequest) {
        PersonDto person = personService.getPersonById(personId);
        SalaryDto salary = salaryService.getPersonCurrentSalary(personId);

        GetPersonResponse response = new GetPersonResponse();
        response.setPerson(person);
        response.setCurrentSalary(salary);
        return response;
    }

    //FIXME: This is temporary endpoint, we will implement pagination later
    @GetMapping("/all")
    public List<PersonDto> getAllPeople() {
        return personService.getAllPeople();
    }
}
