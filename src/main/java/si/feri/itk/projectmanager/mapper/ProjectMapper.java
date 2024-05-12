package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.ProjectList;

@Mapper
public  interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    ProjectDto toDto(Project project);
    @Mapping(target = "ownerId", ignore = true)
    ProjectDto toDto(ProjectList project);
}
