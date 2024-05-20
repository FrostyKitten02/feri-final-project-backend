package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.person.PersonType;
import si.feri.itk.projectmanager.repository.PersonTypeRepo;
import si.feri.itk.projectmanager.util.StringUtil;
import si.feri.itk.projectmanager.util.service.PersonTypeServiceUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonTypeService {
    private final PersonTypeRepo personTypeRepo;
    public UUID createPersonType(CreatePersonTypeRequest request) {
        PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        PersonType personType = PersonTypeServiceUtil.createPersonType(request);
        return personTypeRepo.save(personType).getId();
    }

}
