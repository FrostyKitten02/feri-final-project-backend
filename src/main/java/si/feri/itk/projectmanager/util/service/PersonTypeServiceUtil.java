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
    }

    public static BigDecimal calculateMaxAvailability(CreatePersonTypeRequest request) {
        return calculateMaxAvailability(request.getEducate(), request.getResearch());
    }

    public static BigDecimal calculateMaxAvailability(BigDecimal educate, BigDecimal research) {
        //max bonus of 0.2 when educate is 1 and research is 0
        BigDecimal avalibilityDelta = educate.subtract(research);
        BigDecimal bonusMultiplier = BigDecimal.valueOf(1, 1);
        BigDecimal bonusAvailability = avalibilityDelta.add(BigDecimal.ONE).multiply(bonusMultiplier);
        return research.add(bonusAvailability).setScale(2, RoundingMode.DOWN);
    }

    public static PersonType createPersonType(CreatePersonTypeRequest request) {
        PersonType personType = new PersonType();
        personType.setName(request.getName());
        personType.setResearch(request.getResearch());
        personType.setEducate(request.getEducate());
        personType.setMaxAvailability(calculateMaxAvailability(request));
        return personType;
    }

}
