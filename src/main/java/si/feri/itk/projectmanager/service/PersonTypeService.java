package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.person.PersonType;
import si.feri.itk.projectmanager.repository.PersonTypeRepo;
import si.feri.itk.projectmanager.util.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonTypeService {
    private final PersonTypeRepo personTypeRepo;
    public UUID createPersonType(CreatePersonTypeRequest request) {
        if (StringUtil.isNullOrEmpty(request.getName())) {
            throw new BadRequestException("Name is required");
        }

        BigDecimal total = request.getEducateAvailability().add(request.getResearchAvailability());
        if (total.compareTo(BigDecimal.ONE) >= 1) {
            throw new BadRequestException("Sum of educateAvailability and researchAvailability must be less than or equal to 1");
        }

        //max bonus of 0.2 when educateAvailability is 1 and researchAvailability is 0
        BigDecimal avalibilityDelta = request.getEducateAvailability().subtract(request.getResearchAvailability());
        BigDecimal bonusMultiplier = BigDecimal.valueOf(1, 1);
        BigDecimal bonusAvailability = avalibilityDelta.add(BigDecimal.ONE).multiply(bonusMultiplier);
        BigDecimal availability = request.getResearchAvailability().add(bonusAvailability);


        PersonType personType = new PersonType();
        personType.setName(request.getName());
        personType.setResearch(request.getResearchAvailability());
        personType.setEducate(request.getEducateAvailability());
        personType.setMaxAvailability(availability.setScale(2, RoundingMode.DOWN));
        return personTypeRepo.save(personType).getId();
    }

}
