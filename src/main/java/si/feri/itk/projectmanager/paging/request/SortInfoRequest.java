package si.feri.itk.projectmanager.paging.request;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.paging.SortInfo;


import java.util.List;

public abstract class SortInfoRequest <T extends SortInfo.IField>{
    @Getter
    @Setter
    private boolean ascending = true;
    @Getter
    @Setter
    private List<T> fields;
    private SortInfo<T> defaultSortInfo;

    public SortInfoRequest(boolean ascending, List<T> fields, SortInfo<T> defaultSortInfo) {
        this.ascending = ascending;
        this.fields = fields;
        this.defaultSortInfo = defaultSortInfo;
    }

    public SortInfo<T> toSortInfo() {
        defaultSortInfo.setAscending(ascending);
        defaultSortInfo.setFields(fields);
        return defaultSortInfo;
    }
}
