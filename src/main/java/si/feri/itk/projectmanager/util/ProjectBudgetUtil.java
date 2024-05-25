package si.feri.itk.projectmanager.util;

import si.feri.itk.projectmanager.interfaces.IProjectBudgetRequest;
import si.feri.itk.projectmanager.model.ProjectBudgetSchema;

import java.math.BigDecimal;

public class ProjectBudgetUtil {
    private ProjectBudgetUtil() {}

    public static BigDecimal calculateIndirectBudget(IProjectBudgetRequest budget, ProjectBudgetSchema schema) {
        return budget.getStaffBudget()
                .add(budget.getTravelBudget())
                .add(budget.getEquipmentBudget())
                .multiply(schema.getIndirectBudget());
    }

}
