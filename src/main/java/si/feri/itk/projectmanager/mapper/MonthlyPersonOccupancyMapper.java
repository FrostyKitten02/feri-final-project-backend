package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.MonthlyPersonOccupancyDto;
import si.feri.itk.projectmanager.model.MonthlyPersonOccupancy;

@Mapper
public interface MonthlyPersonOccupancyMapper {
    MonthlyPersonOccupancyMapper INSTANCE = Mappers.getMapper(MonthlyPersonOccupancyMapper.class);
    MonthlyPersonOccupancyDto toDto(MonthlyPersonOccupancy monthlyPersonOccupancy);
}
