package si.feri.itk.projectmanager.dto.sortinforequest;

import si.feri.itk.projectmanager.paging.request.SortInfoRequest;
import si.feri.itk.projectmanager.paging.ProjectSortInfo;

import java.util.List;

public class ProjectSortInfoRequest extends SortInfoRequest<ProjectSortInfo.Field> {
    public ProjectSortInfoRequest(boolean ascending, List<ProjectSortInfo.Field> fields) {
        super(ascending, fields, new ProjectSortInfo(fields, ascending));
    }
}
