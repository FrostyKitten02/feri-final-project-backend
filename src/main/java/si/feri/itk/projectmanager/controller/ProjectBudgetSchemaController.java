package si.feri.itk.projectmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.feri.itk.projectmanager.dto.response.ListProjectBudgetSchemaResponse;
import si.feri.itk.projectmanager.service.ProjectBudgetSchemaService;

@CrossOrigin
@RestController
@RequestMapping("/project-budget-schema")
@RequiredArgsConstructor
public class ProjectBudgetSchemaController {
    private final ProjectBudgetSchemaService projectBudgetSchemaService;


    @GetMapping("/list")
    public ListProjectBudgetSchemaResponse getAllProjectBudgetSchema() {
        ListProjectBudgetSchemaResponse response = new ListProjectBudgetSchemaResponse();
        response.setProjectBudgetSchemaDtoList(projectBudgetSchemaService.getAllProjectBudgetSchema());
        return response;
    }
}
