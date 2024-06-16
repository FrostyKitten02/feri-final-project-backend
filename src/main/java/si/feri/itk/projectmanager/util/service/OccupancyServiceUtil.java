package si.feri.itk.projectmanager.util.service;

import si.feri.itk.projectmanager.dto.request.CreateOccupancyRequest;
import si.feri.itk.projectmanager.dto.request.UpdateOccupancyRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.model.Occupancy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OccupancyServiceUtil {

    private OccupancyServiceUtil() {
    }

    public static void validateCreateOccupancyRequest(CreateOccupancyRequest request) {
        if (request.getFromMonth() == null) {
            throw new BadRequestException("From month is required.");
        }

        if (request.getPersonId() == null) {
            throw new BadRequestException("Person id is required.");
        }

        if (request.getProjectId() == null) {
            throw new BadRequestException("Project id is required.");
        }

        if (request.getValue() == null || request.getValue().compareTo(BigDecimal.ZERO) >= 0) {
            throw new BadRequestException("Value is required.");
        }
    }

    public static void validateUpdateOccupancyRequest(UpdateOccupancyRequest request) {
        if (request.getOccupancyId() == null) {
            throw new BadRequestException("Occupancy id is required.");
        }

        if (request.getValue() == null || request.getValue().compareTo(BigDecimal.ZERO) >= 0) {
            throw new BadRequestException("Value is required.");
        }
    }


    public static List<Occupancy> createOccupancies(CreateOccupancyRequest request) {
        if (request.getToMonth() == null) {
            return Collections.singletonList(createOccupancyFromRequest(request));
        }

        LocalDate fromMonth = request.getFromMonth().withDayOfMonth(1);
        LocalDate toMonth = request.getToMonth().withDayOfMonth(1);

        final int diff = Period.between(fromMonth, toMonth).getMonths();
        final List<Occupancy> occupancies = new ArrayList<>(diff);
        do {
            occupancies.add(createOccupancy(request, fromMonth));
            fromMonth = fromMonth.plusMonths(1);
        } while (!fromMonth.isAfter(toMonth));


        if (occupancies.size() > diff + 1) {
            throw new InternalServerException("Error while creating occupancies, number of created occupancies is greater than expected.");
        }

        return occupancies;
    }


    public static Occupancy createOccupancyFromRequest(CreateOccupancyRequest request) {
        return createOccupancy(request, request.getFromMonth());
    }

    public static Occupancy createOccupancy(CreateOccupancyRequest request, LocalDate month) {
        Occupancy occupancy = new Occupancy();
        occupancy.setPersonId(request.getPersonId());
        occupancy.setProjectId(request.getProjectId());
        occupancy.setMonth(month);
        occupancy.setValue(request.getValue());
        return occupancy;
    }

}
