package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.salary.SalaryDto;
import si.feri.itk.projectmanager.dto.model.salary.SalaryListDto;
import si.feri.itk.projectmanager.model.person.Salary;
import si.feri.itk.projectmanager.model.person.SalaryList;

@Mapper
public interface SalaryMapper {
    SalaryMapper INSTANCE = Mappers.getMapper(SalaryMapper.class);
    SalaryDto toDto(Salary salary);
    SalaryListDto toDto(SalaryList salary);
}
