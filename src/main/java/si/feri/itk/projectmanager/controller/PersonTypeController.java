package si.feri.itk.projectmanager.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.request.persontype.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.dto.request.persontype.PersonTypeListSearchParams;
import si.feri.itk.projectmanager.dto.request.persontype.PersonTypeListSortInfoRequest;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.dto.response.persontype.ListPersonTypeResponse;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.service.PersonTypeService;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/person-type")
@RequiredArgsConstructor
public class PersonTypeController {
    private final PersonTypeService personTypeService;

    @PostMapping
    public ResourceCreatedResponse createPersonType(@RequestBody CreatePersonTypeRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        UUID personTypeId = personTypeService.createPersonType(request, servletRequest);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setId(personTypeId);
        return response;
    }

    @GetMapping("list")
    public ListPersonTypeResponse listPersonTypes(
            @NotNull PageInfoRequest pageInfo,
            @Nullable PersonTypeListSortInfoRequest sortInfo,
            @Nullable PersonTypeListSearchParams searchParams
    ) {
        return personTypeService.searchPersonTypes(pageInfo, sortInfo, searchParams);
    }

}
