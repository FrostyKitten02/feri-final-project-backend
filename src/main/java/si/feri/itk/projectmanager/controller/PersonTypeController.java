package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.PersonTypeService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/person-type")
@RequiredArgsConstructor
public class PersonTypeController {
    private final PersonTypeService personTypeService;

    @PostMapping
    public ResourceCreatedResponse createPersonType(@RequestBody CreatePersonTypeRequest request, HttpServletResponse servletResponse) {
        UUID personTypeId = personTypeService.createPersonType(request);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setId(personTypeId);
        return response;
    }
}
