package si.feri.itk.projectmanager.dto.response.salary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import si.feri.itk.projectmanager.dto.model.salary.SalaryDto;
import si.feri.itk.projectmanager.dto.model.salary.SalaryListDto;
import si.feri.itk.projectmanager.mapper.SalaryMapper;
import si.feri.itk.projectmanager.model.person.SalaryList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.ProjectSortInfo;
import si.feri.itk.projectmanager.paging.response.SortInfoResponse;

import java.util.List;

@Getter
@Setter
public class ListSalaryResponse {
    private List<SalaryListDto> salaries;
    private PageInfo pageInfo;
    private SortInfoResponse<?> sortInfo;

    public static ListSalaryResponse fromPage(Page<SalaryList> page) {
        List<SalaryListDto> salaries = page.getContent().stream().map(SalaryMapper.INSTANCE::toDto).toList();
        PageInfo pageInfoRes = PageInfo.from(page);

        ProjectSortInfo projectSortInfo = ProjectSortInfo.fromPage(page);
        SortInfoResponse<ProjectSortInfo.Field> sortInfoRes;
        if (projectSortInfo != null) {
            sortInfoRes = projectSortInfo.toSortInfoResponse();
        } else {
            sortInfoRes = null;
        }

        ListSalaryResponse res = new ListSalaryResponse();
        res.setSalaries(salaries);
        res.setPageInfo(pageInfoRes);
        res.setSortInfo(sortInfoRes);
        return res;
    }
}
