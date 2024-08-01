package si.feri.itk.projectmanager.dto.response.persontype;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import si.feri.itk.projectmanager.dto.model.PersonTypeListDto;
import si.feri.itk.projectmanager.mapper.PersonTypeMapper;
import si.feri.itk.projectmanager.model.person.PersonTypeList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.ProjectSortInfo;
import si.feri.itk.projectmanager.paging.response.SortInfoResponse;

import java.util.List;

@Getter
@Setter
public class ListPersonTypeResponse {
    private List<PersonTypeListDto> personTypes;
    private PageInfo pageInfo;
    private SortInfoResponse<?> sortInfo;

    public static ListPersonTypeResponse fromPage(Page<PersonTypeList> page) {
        List<PersonTypeListDto> types = page.getContent().stream().map(PersonTypeMapper.INSTANCE::toDto).toList();
        PageInfo pageInfoRes = PageInfo.from(page);

        ProjectSortInfo projectSortInfo = ProjectSortInfo.fromPage(page);
        SortInfoResponse<ProjectSortInfo.Field> sortInfoRes;
        if (projectSortInfo != null) {
            sortInfoRes = projectSortInfo.toSortInfoResponse();
        } else {
            sortInfoRes = null;
        }

        ListPersonTypeResponse res = new ListPersonTypeResponse();
        res.setPersonTypes(types);
        res.setPageInfo(pageInfoRes);
        res.setSortInfo(sortInfoRes);
        return res;
    }
}
