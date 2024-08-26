package si.feri.itk.projectmanager.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.model.person.PersonDto;
import si.feri.itk.projectmanager.dto.model.salary.SalaryDto;
import si.feri.itk.projectmanager.dto.request.person.PersonListSearchParams;
import si.feri.itk.projectmanager.dto.request.person.PersonSortInfoRequest;
import si.feri.itk.projectmanager.dto.response.person.GetPersonResponse;
import si.feri.itk.projectmanager.dto.response.person.ListPersonResponse;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.service.PersonService;
import si.feri.itk.projectmanager.service.SalaryService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final SalaryService salaryService;

    @GetMapping("/current")
    public GetPersonResponse getCurrentPerson(HttpServletRequest servletRequest) {
        PersonDto person = personService.getCurrentPerson(servletRequest);
        SalaryDto salary = salaryService.getPersonCurrentSalary(person.getId());

        GetPersonResponse response = new GetPersonResponse();
        response.setPerson(person);
        response.setCurrentSalary(salary);
        return response;
    }

    @GetMapping("/{personId}")
    public GetPersonResponse getPersonById(@PathVariable UUID personId, HttpServletRequest servletRequest) {
        PersonDto person = personService.getPersonById(personId);
        SalaryDto salary = salaryService.getPersonCurrentSalary(personId);

        GetPersonResponse response = new GetPersonResponse();
        response.setPerson(person);
        response.setCurrentSalary(salary);
        return response;
    }

    @GetMapping("/list")
    public ListPersonResponse listPeople(
            @NotNull PageInfoRequest pageInfo,
            @Nullable PersonSortInfoRequest sortInfo,
            @Nullable PersonListSearchParams searchParams
    ) {
        return personService.searchPeople(pageInfo, sortInfo, searchParams);
    }
}
