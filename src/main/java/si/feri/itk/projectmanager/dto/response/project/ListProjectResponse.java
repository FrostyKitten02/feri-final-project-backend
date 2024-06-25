package si.feri.itk.projectmanager.dto.response.project;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.mapper.ProjectMapper;
import si.feri.itk.projectmanager.model.ProjectList;
import si.feri.itk.projectmanager.paging.PageInfo;
import si.feri.itk.projectmanager.paging.ProjectSortInfo;
import si.feri.itk.projectmanager.paging.response.SortInfoResponse;

import java.util.List;

@Getter
@Setter
public class ListProjectResponse {
    private List<ProjectDto> projects;
    private PageInfo pageInfo;
    private SortInfoResponse<?> sortInfo;

    public static ListProjectResponse fromPage(Page<ProjectList> page) {
        List<ProjectDto> projects = page.getContent().stream().map(ProjectMapper.INSTANCE::toDto).toList();
        PageInfo pageInfoRes = PageInfo.from(page);

        ProjectSortInfo projectSortInfo = ProjectSortInfo.fromPage(page);
        SortInfoResponse<ProjectSortInfo.Field> sortInfoRes;
        if (projectSortInfo != null) {
            sortInfoRes = projectSortInfo.toSortInfoResponse();
        } else {
            sortInfoRes = null;
        }

        ListProjectResponse res = new ListProjectResponse();
        res.setProjects(projects);
        res.setPageInfo(pageInfoRes);
        res.setSortInfo(sortInfoRes);
        return res;
    }


}
