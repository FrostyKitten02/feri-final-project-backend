package si.feri.itk.projectmanager.interfaces;

import java.math.BigDecimal;

public interface IProjectBudgetRequest {
    BigDecimal getStaffBudget();
    BigDecimal getTravelBudget();
    BigDecimal getEquipmentBudget();
    BigDecimal getSubcontractBudget();

    void setStaffBudget(BigDecimal staffBudget);
    void setTravelBudget(BigDecimal travelBudget);
    void setEquipmentBudget(BigDecimal equipmentBudget);
    void setSubcontractBudget(BigDecimal subcontractBudget);
}
