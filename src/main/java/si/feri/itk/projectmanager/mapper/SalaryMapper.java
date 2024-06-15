package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.SalaryDto;
import si.feri.itk.projectmanager.model.person.Salary;

@Mapper
public interface SalaryMapper {
    SalaryMapper INSTANCE = Mappers.getMapper(SalaryMapper.class);

    SalaryDto toDto(Salary salary);
}
