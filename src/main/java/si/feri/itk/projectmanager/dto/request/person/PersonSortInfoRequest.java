package si.feri.itk.projectmanager.dto.request.person;

import si.feri.itk.projectmanager.paging.PersonSortInfo;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.SortInfoRequest;

import java.util.List;

public class PersonSortInfoRequest extends SortInfoRequest<PersonSortInfo.Field> {
    public PersonSortInfoRequest(boolean ascending, List<PersonSortInfo.Field> fields) {
        super(ascending, fields, new PersonSortInfo(fields, ascending));
    }
}
