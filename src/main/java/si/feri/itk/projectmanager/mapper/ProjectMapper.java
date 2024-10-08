package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.project.ProjectList;

@Mapper
public  interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    ProjectDto toDto(Project project);

    ProjectDto toDto(ProjectList project);
}
