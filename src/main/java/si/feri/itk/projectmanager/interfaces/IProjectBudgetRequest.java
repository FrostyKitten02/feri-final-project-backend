package si.feri.itk.projectmanager.interfaces;

import java.math.BigDecimal;
import java.util.UUID;

public interface IProjectBudgetRequest {
    UUID getProjectBudgetSchemaId();
    BigDecimal getStaffBudget();
    BigDecimal getTravelBudget();
    BigDecimal getEquipmentBudget();
    BigDecimal getSubcontractingBudget();

    void setStaffBudget(BigDecimal staffBudget);
    void setTravelBudget(BigDecimal travelBudget);
    void setEquipmentBudget(BigDecimal equipmentBudget);
    void setSubcontractingBudget(BigDecimal subcontractBudget);
}
