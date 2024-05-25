package si.feri.itk.projectmanager.interfaces;

import java.math.BigDecimal;

public interface IProjectBudget extends IProjectBudgetRequest {
    BigDecimal getIndirectBudget();
    void setIndirectBudget(BigDecimal indirectBudget);
}
