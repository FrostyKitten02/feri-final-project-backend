package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.model.ProjectBudgetSchemaDto;
import si.feri.itk.projectmanager.mapper.ProjectBudgetSchemaMapper;
import si.feri.itk.projectmanager.repository.ProjectBudgetSchemaRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectBudgetSchemaService {
    private final ProjectBudgetSchemaRepo projectBudgetSchemaRepo;

    public List<ProjectBudgetSchemaDto> getAllProjectBudgetSchema() {
        return projectBudgetSchemaRepo.findAll().stream().map(ProjectBudgetSchemaMapper.INSTANCE::toDto).toList();
    }
}
