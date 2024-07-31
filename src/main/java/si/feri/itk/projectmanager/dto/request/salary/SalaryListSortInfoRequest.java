package si.feri.itk.projectmanager.dto.request.salary;

import si.feri.itk.projectmanager.paging.SalaryListSortInfo;
import si.feri.itk.projectmanager.paging.request.SortInfoRequest;

import java.util.List;

public class SalaryListSortInfoRequest extends SortInfoRequest<SalaryListSortInfo.Field> {
    public SalaryListSortInfoRequest(boolean ascending, List<SalaryListSortInfo.Field> fields) {
        super(ascending, fields, new SalaryListSortInfo(fields, ascending));
    }
}
