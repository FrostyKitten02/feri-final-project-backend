package si.feri.itk.projectmanager.dto.response;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.dto.model.ProjectBudgetSchemaDto;

import java.util.List;

@Getter
@Setter
public class ListProjectBudgetSchemaResponse {
    private List<ProjectBudgetSchemaDto> projectBudgetSchemaDtoList;
}
