package si.feri.itk.projectmanager.dto.request.persontype;

import si.feri.itk.projectmanager.paging.PersonTypeListSortInfo;
import si.feri.itk.projectmanager.paging.request.SortInfoRequest;

import java.util.List;

public class PersonTypeListSortInfoRequest extends SortInfoRequest<PersonTypeListSortInfo.Field> {
    public PersonTypeListSortInfoRequest(boolean ascending, List<PersonTypeListSortInfo.Field> fields) {
        super(ascending, fields, new PersonTypeListSortInfo(fields, ascending));
    }
}
