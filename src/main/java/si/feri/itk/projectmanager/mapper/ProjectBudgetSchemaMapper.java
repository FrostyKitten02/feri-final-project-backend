package si.feri.itk.projectmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import si.feri.itk.projectmanager.dto.model.ProjectBudgetSchemaDto;
import si.feri.itk.projectmanager.model.ProjectBudgetSchema;

@Mapper
public interface ProjectBudgetSchemaMapper {
    ProjectBudgetSchemaMapper INSTANCE = Mappers.getMapper(ProjectBudgetSchemaMapper.class);
    ProjectBudgetSchemaDto toDto(ProjectBudgetSchema projectBudgetSchema);
}
