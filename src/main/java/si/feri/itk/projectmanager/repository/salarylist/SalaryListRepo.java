package si.feri.itk.projectmanager.repository.salarylist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.feri.itk.projectmanager.model.person.SalaryList;

import java.util.UUID;

@Repository
public interface SalaryListRepo extends JpaRepository<SalaryList, UUID>, CustomSalaryListRepo {
}
