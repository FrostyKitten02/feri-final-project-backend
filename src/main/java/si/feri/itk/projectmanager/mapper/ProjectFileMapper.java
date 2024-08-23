package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.ProjectFileDto;
import si.feri.itk.projectmanager.model.project.ProjectFile;

@Mapper
public interface ProjectFileMapper {
    ProjectFileMapper INSTANCE = Mappers.getMapper(ProjectFileMapper.class);

    ProjectFileDto toDto(ProjectFile projectFile);
}
