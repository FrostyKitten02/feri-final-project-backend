package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.salary.SalaryDtoImpl;
import si.feri.itk.projectmanager.model.person.Salary;
import si.feri.itk.projectmanager.model.person.SalaryList;

@Mapper
public interface SalaryMapper {
    SalaryMapper INSTANCE = Mappers.getMapper(SalaryMapper.class);

    SalaryDtoImpl toDto(Salary salary);
    SalaryDtoImpl toDto(SalaryList salary);
}
