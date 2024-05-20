package si.feri.itk.projectmanager.util.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import si.feri.itk.projectmanager.dto.request.CreatePersonTypeRequest;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;
import si.feri.itk.projectmanager.model.person.PersonType;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class PersonTypeServiceUtilTest {

    @Test
    public void testValidateCreatePersonTypeRequest() {
        CreatePersonTypeRequest request = new CreatePersonTypeRequest();
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        });

        request.setName("Test name");
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        });

        request.setEducate(BigDecimal.valueOf(5, 1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        });

        request.setResearch(BigDecimal.valueOf(6, 1));
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        });


        request.setResearch(BigDecimal.valueOf(5, 1));
        Assertions.assertDoesNotThrow(() -> {
            PersonTypeServiceUtil.validateCreatePersonTypeRequest(request);
        });
    }

    @Test
    public void calculateMaxAvailabilityTests() {
        calculateMaxAvailabilityTest(50, 50, 60);
        calculateMaxAvailabilityTest(0, 100, 100);
        calculateMaxAvailabilityTest(100, 0, 20);
        calculateMaxAvailabilityTest(60, 40, 52);
    }

    private void calculateMaxAvailabilityTest(long educate, long research, long expected) {
        int scale = 2;
        BigDecimal educateAvailability = BigDecimal.valueOf(educate, scale);
        BigDecimal researchAvailability = BigDecimal.valueOf(research, scale);
        BigDecimal result = PersonTypeServiceUtil.calculateMaxAvailability(educateAvailability, researchAvailability);
        BigDecimal expectedResult = BigDecimal.valueOf(expected, scale);
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void createPersonTypeTest() {
        CreatePersonTypeRequest request = new CreatePersonTypeRequest();
        request.setName("Test name");
        request.setEducate(BigDecimal.valueOf(50, 2));
        request.setResearch(BigDecimal.valueOf(50, 2));
        PersonType personType = PersonTypeServiceUtil.createPersonType(request);
        Assertions.assertEquals(request.getName(), personType.getName());
        Assertions.assertEquals(request.getEducate(), personType.getEducate());
        Assertions.assertEquals(request.getResearch(), personType.getResearch());
        Assertions.assertNotNull(personType.getMaxAvailability());
    }

}