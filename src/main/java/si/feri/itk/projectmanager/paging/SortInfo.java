package si.feri.itk.projectmanager.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import si.feri.itk.projectmanager.paging.response.SortInfoResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public abstract class SortInfo <T extends SortInfo.IField> {
    private boolean ascending;
    private List<T> fields;
    @JsonIgnore
    private T defaultField;

    public Sort getSort() {
        if (fields == null || fields.isEmpty()) {
            return Sort.by(defaultField.getColumnName());
        }

        Sort sorting = Sort.by(fields.stream().map(T::getColumnName).toArray(String[]::new));
        if (ascending) {
            return sorting.ascending();
        }

        return sorting.descending();
    }

    public SortInfoResponse<T> toSortInfoResponse() {
        SortInfoResponse<T> sortInfoResponse = new SortInfoResponse<>();
        sortInfoResponse.setAscending(this.isAscending());
        sortInfoResponse.setFields(this.getFields());
        return sortInfoResponse;
    }

    public interface IField {
        String getColumnName();
    }
}