package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.person.PersonType;
import si.feri.itk.projectmanager.util.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PersonTypeServiceUtil {
    private PersonTypeServiceUtil() {}

    public static void validateCreatePersonTypeRequest(CreatePersonTypeRequest request) {
        if (StringUtil.isNullOrEmpty(request.getName())) {
            throw new BadRequestException("Name is required");
        }

        if (request.getEducate() == null) {
            throw new BadRequestException("Educate availability is required");
        }

        if (request.getResearch() == null) {
            throw new BadRequestException("Research availability is required");
        }

        BigDecimal total = request.getEducate().add(request.getResearch());
        if (total.compareTo(BigDecimal.ONE) >= 1) {
            throw new BadRequestException("Sum of educateAvailability and researchAvailability must be less than or equal to 1");
        }

        if (request.getPersonId() == null) {
            throw new BadRequestException("Person id is required");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() != null && request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("End date must be after start date");
        }
    }

    public static BigDecimal calculateMaxAvailability(CreatePersonTypeRequest request) {
        return calculateMaxAvailability(request.getEducate(), request.getResearch());
    }

    public static BigDecimal calculateMaxAvailability(BigDecimal educate, BigDecimal research) {
        //max bonus of 0.2 when educate is 1 and research is 0
        BigDecimal bonusMultiplier = BigDecimal.valueOf(2, 1);//20%
        BigDecimal bonusAvailability = educate.multiply(bonusMultiplier);//20% of educate
        return research.add(bonusAvailability).setScale(2, RoundingMode.DOWN);
    }

    public static PersonType createPersonType(CreatePersonTypeRequest request) {
        PersonType personType = new PersonType();
        personType.setName(request.getName());
        personType.setResearch(request.getResearch());
        personType.setEducate(request.getEducate());
        personType.setStartDate(request.getStartDate());
        personType.setEndDate(request.getEndDate());
        personType.setPersonId(request.getPersonId());
        personType.setMaxAvailability(calculateMaxAvailability(request));
        return personType;
    }

}
