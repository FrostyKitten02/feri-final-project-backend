package si.feri.itk.projectmanager.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.model.salary.SalaryDto;
import si.feri.itk.projectmanager.dto.request.salary.CreateSalaryRequest;
import si.feri.itk.projectmanager.dto.request.salary.SalaryListSearchParams;
import si.feri.itk.projectmanager.dto.request.salary.SalaryListSortInfoRequest;
import si.feri.itk.projectmanager.dto.response.salary.ListSalaryResponse;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.exceptions.implementation.UnauthorizedException;
import si.feri.itk.projectmanager.mapper.SalaryMapper;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.person.Salary;
import si.feri.itk.projectmanager.model.person.SalaryList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.PageInfoRequest;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.SalaryRepo;
import si.feri.itk.projectmanager.repository.salarylist.SalaryListRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.SalaryServiceUtil;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepo salaryRepo;
    private final PersonRepo personRepo;
    private final SalaryListRepo salaryListRepo;
    public SalaryDto getPersonCurrentSalary(UUID personId) {
        Optional<Salary> salaryOptional = salaryRepo.findLastByPersonId(personId);
        if (salaryOptional.isEmpty()) {
            return null;
        }

        Salary salary = salaryOptional.get();
        LocalDate now = LocalDate.now();
        if (salary.getEndDate() != null && salary.getEndDate().isBefore(now)) {
            return null;
        }

        return SalaryMapper.INSTANCE.toDto(salaryOptional.get());
    }


    @Transactional
    public UUID addSalaryToPerson(CreateSalaryRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);

        Person person = personRepo.findById(UUID.fromString(userId)).orElseThrow(() -> new UnauthorizedException("User not found"));
        if (!person.isAdmin()) {
            throw new UnauthorizedException("Permission denied");
        }
        SalaryServiceUtil.validateCreateSalaryRequest(request);

        moveConflictingSalary(request);

        Salary salary = SalaryServiceUtil.createSalary(request);
        return salaryRepo.save(salary).getId();
    }


    private void moveConflictingSalary(CreateSalaryRequest request) {
        Optional<Salary> conflictingSalaryOptional = salaryRepo.findLatestByPersonIdAndStartDateIsBeforeOrEqual(request.getPersonId(), request.getStartDate());
        if (conflictingSalaryOptional.isEmpty()) {
            return;
        }
        Salary conflictiongSalary = conflictingSalaryOptional.get();

        if (conflictiongSalary.getStartDate().equals(request.getStartDate())) {
            throw new BadRequestException("Salary for this person already exists for this start date");
        }
        //if request start date is in between existing salary we override end date regardless if salary end date is null
        //TODO check if this is intended behavior or should we throw an exception and not let user override existing salary with limited end date
        conflictiongSalary.setEndDate(request.getStartDate().minusDays(1));

        //just in case something unexpected happens!!!
        if (conflictiongSalary.getEndDate().isBefore(conflictiongSalary.getStartDate())) {
            throw new InternalServerException("Error creating salary");
        }

        salaryRepo.save(conflictiongSalary);
    }

    public ListSalaryResponse searchSalaries(PageInfoRequest pageInfoRequest, SalaryListSortInfoRequest sortInfoRequest, SalaryListSearchParams searchParams) {
        SortInfo<?> sort = RequestUtil.getSortInfoFromRequest(sortInfoRequest);
        Page<SalaryList> salariesPage = salaryListRepo.searchSalaries(searchParams, PageInfo.toPageRequest(pageInfoRequest, sort));
        return ListSalaryResponse.fromPage(salariesPage);
    }

}
