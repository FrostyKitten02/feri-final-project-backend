package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.CreateSalaryRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.model.person.Salary;

import java.math.BigDecimal;

public class SalaryServiceUtil {
    private SalaryServiceUtil() {}

    public static void validateCreateSalaryRequest(CreateSalaryRequest request) {
        if (request.getPersonId() == null) {
            throw new BadRequestException("Person id is required");
        }

        if (request.getAmount() == null || request.getAmount().equals(BigDecimal.ZERO)) {
            throw new BadRequestException("Amount is required");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Star date is required");
        }

        if (request.getEndDate() != null && request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("End date must be after start date!");
        }
    }

    public static Salary createSalary(CreateSalaryRequest createSalary) {
        Salary salary = new Salary();
        salary.setPersonId(createSalary.getPersonId());
        salary.setAmount(createSalary.getAmount());
        salary.setStartDate(createSalary.getStartDate());
        salary.setEndDate(createSalary.getEndDate());
        return salary;
    }
}