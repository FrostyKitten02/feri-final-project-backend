package si.feri.itk.projectmanager.repository.salarylist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import si.feri.itk.projectmanager.dto.request.salary.SalaryListSearchParams;
import si.feri.itk.projectmanager.model.person.SalaryList;

public interface CustomSalaryListRepo {
    Page<SalaryList> searchSalaries(SalaryListSearchParams searchParams, Pageable pageable);
}
