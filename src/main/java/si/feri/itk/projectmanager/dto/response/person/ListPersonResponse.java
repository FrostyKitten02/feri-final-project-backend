package si.feri.itk.projectmanager.dto.response.person;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import si.feri.itk.projectmanager.dto.model.person.PersonListDto;
import si.feri.itk.projectmanager.mapper.PersonMapper;
import si.feri.itk.projectmanager.model.person.PersonList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.PersonSortInfo;
import si.feri.itk.projectmanager.paging.response.SortInfoResponse;

import java.util.List;

@Getter
@Setter
public class ListPersonResponse {
    private List<PersonListDto> people;
    private PageInfo pageInfo;
    private SortInfoResponse<?> sortInfo;


    public static ListPersonResponse fromPage(Page<PersonList> page) {
        List<PersonListDto> projects = page.getContent().stream().map(PersonMapper.INSTANCE::toDto).toList();
        PageInfo pageInfoRes = PageInfo.from(page);

        PersonSortInfo projectSortInfo = PersonSortInfo.fromPage(page);
        SortInfoResponse<PersonSortInfo.Field> sortInfoRes;
        if (projectSortInfo != null) {
            sortInfoRes = projectSortInfo.toSortInfoResponse();
        } else {
            sortInfoRes = null;
        }

        ListPersonResponse res = new ListPersonResponse();
        res.setPeople(projects);
        res.setPageInfo(pageInfoRes);
        res.setSortInfo(sortInfoRes);
        return res;
    }
}
