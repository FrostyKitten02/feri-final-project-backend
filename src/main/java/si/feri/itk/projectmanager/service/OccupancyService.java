package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import si.feri.itk.projectmanager.dto.model.MonthlyPersonOccupancyDto;
import si.feri.itk.projectmanager.dto.request.occupancy.CreateOccupancyRequest;
import si.feri.itk.projectmanager.dto.request.occupancy.UpdateOccupancyRequest;
import si.feri.itk.projectmanager.dto.response.occupancy.CreateOccupancyResponse;
import si.feri.itk.projectmanager.dto.response.occupancy.MonthlyPersonOccupancyResponse;
import si.feri.itk.projectmanager.dto.response.occupancy.OccupancyWarning;
import si.feri.itk.projectmanager.exceptions.implementation.IllegalResourceAccess;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.MonthlyPersonOccupancyMapper;
import si.feri.itk.projectmanager.model.MonthlyPersonOccupancy;
import si.feri.itk.projectmanager.model.Occupancy;
import si.feri.itk.projectmanager.model.person.PersonType;
import si.feri.itk.projectmanager.repository.MonthlyPersonOccupancyRepo;
import si.feri.itk.projectmanager.repository.OccupancyRepo;
import si.feri.itk.projectmanager.repository.PersonOnProjectRepo;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.PersonTypeRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.OccupancyServiceUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OccupancyService {
    private final PersonRepo personRepo;
    private final ProjectRepo projectRepo;
    private final OccupancyRepo occupancyRepo;
    private final PersonTypeRepo personTypeRepo;
    private final PersonOnProjectRepo personOnProjectRepo;
    private final MonthlyPersonOccupancyRepo monthlyPersonOccupancyRepo;

    public MonthlyPersonOccupancyResponse getMonthlyPersonOccupancy(LocalDate fromMonth, LocalDate toMonth, UUID personId) {
        List<MonthlyPersonOccupancy> monthlyOccupancies = monthlyPersonOccupancyRepo.findAllByMonthIsBetweenAndPersonIdEquals(fromMonth, toMonth, personId);
        List<MonthlyPersonOccupancyDto> monthlyOccupancyDto = monthlyOccupancies.stream().map(MonthlyPersonOccupancyMapper.INSTANCE::toDto).toList();

        MonthlyPersonOccupancyResponse response = new MonthlyPersonOccupancyResponse();
        response.setMonthlyPersonOccupancies(monthlyOccupancyDto);
        return response;
    }

    @Transactional
    public void deleteOccupancy(@NotNull UUID occupancyId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        Occupancy occupancy = occupancyRepo.findById(occupancyId).orElseThrow(() -> new ItemNotFoundException("Occupancy not found"));
        projectRepo.findByIdAndOwnerId(occupancy.getProjectId(), userId).orElseThrow(() -> new IllegalResourceAccess("You are not allowed to update this occupancy"));

        occupancyRepo.delete(occupancy);
    }


    @Transactional
    public void updateOccupancy(@Validated UpdateOccupancyRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        OccupancyServiceUtil.validateUpdateOccupancyRequest(request);

        Occupancy occupancy = occupancyRepo.findById(request.getOccupancyId()).orElseThrow(() -> new ItemNotFoundException("Occupancy not found"));
        projectRepo.findByIdAndOwnerId(occupancy.getProjectId(), userId).orElseThrow(() -> new IllegalResourceAccess("You are not allowed to update this occupancy"));

        occupancy.setValue(request.getValue());
        occupancyRepo.save(occupancy);
    }

    @Transactional
    public CreateOccupancyResponse addOccupancy(@Validated CreateOccupancyRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        OccupancyServiceUtil.validateCreateOccupancyRequest(request);

        projectRepo.findByIdAndOwnerId(request.getProjectId(), userId)
                .orElseThrow(() -> new ItemNotFoundException("Project not found"));

        personOnProjectRepo.findFirstByProjectIdAndPersonId(request.getProjectId(), request.getPersonId())
                .orElseThrow(() -> new ItemNotFoundException("Person not found on project"));


        List<Occupancy> occupancies = OccupancyServiceUtil.createOccupancies(request);
        occupancyRepo.deleteAllByMonthInAndProjectIdAndPersonId(occupancies.stream().map(Occupancy::getMonth).toList(), request.getProjectId(), request.getPersonId());

        List<Occupancy> saved = occupancyRepo.saveAll(occupancies);
        List<OccupancyWarning> warnings = createOccupancyWarnings(saved);
        CreateOccupancyResponse response = new CreateOccupancyResponse();
        response.setIds(saved.stream().map(Occupancy::getId).toList());
        response.setOccupancyWarnings(warnings);
        return response;
    }


    private List<OccupancyWarning> createOccupancyWarnings(List<Occupancy> newlyAdded) {
        List<OccupancyWarning> warnings = new ArrayList<>(newlyAdded.size());
        for (Occupancy occupancy : newlyAdded) {
            BigDecimal sum = occupancyRepo.sumAllByMonthAndPersonId(occupancy.getMonth(), occupancy.getPersonId());
            PersonType personType = personTypeRepo.findByStartDateBeforeAndEndDateNullOrAfterAndPersonId(occupancy.getMonth(), occupancy.getPersonId());

            if (personType != null && personType.getMaxAvailability().compareTo(sum) > 0) {
                continue;
            }

            OccupancyWarning warning = new OccupancyWarning();
            warning.setMonth(occupancy.getMonth());
            warning.setAllocatedOccupancy(sum);
            if (personType != null) {
                warning.setMaxOccupancy(personType.getMaxAvailability());
            } else {
                //if person type is not defined we don't return max occupancy but raise flag to inform user that it is not defined
                warning.setAvalibilityNotDefined(true);
            }
            warnings.add(warning);
        }

        return warnings;
    }


}
