package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.UnauthorizedException;
import si.feri.itk.projectmanager.model.person.PersonType;
import si.feri.itk.projectmanager.repository.PersonTypeRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StringUtil;
import si.feri.itk.projectmanager.util.service.PersonTypeServiceUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonTypeService {
    private final PersonTypeRepo personTypeRepo;
    @Value("${admin-clerk-id}")
    private String adminId;
    public UUID createPersonType(CreatePersonTypeRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        if (!userId.equals(adminId)) {
            throw new UnauthorizedException("Permission denied");
        }

        PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        PersonType personType = PersonTypeServiceUtil.createPersonType(request);
        return personTypeRepo.save(personType).getId();
    }

}
