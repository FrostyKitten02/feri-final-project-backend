package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.WorkPackageDto;
import si.feri.itk.projectmanager.dto.response.statistics.WorkPackageWithStatisticsDto;
import si.feri.itk.projectmanager.model.WorkPackage;

@Mapper
public interface WorkPackageMapper {
    WorkPackageMapper INSTANCE = Mappers.getMapper(WorkPackageMapper.class);

    WorkPackageDto toDto(WorkPackage workPackage);
    WorkPackageWithStatisticsDto toDtoWithStatistics(WorkPackage workPackage);
}
