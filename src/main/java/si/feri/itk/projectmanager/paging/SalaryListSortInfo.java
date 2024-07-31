package si.feri.itk.projectmanager.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SalaryListSortInfo extends SortInfo<SalaryListSortInfo.Field>{
    @Getter
    @JsonIgnore
    private static final Map<String, SalaryListSortInfo.Field> fieldMap = new HashMap<>();

    static {
        Arrays.stream(SalaryListSortInfo.Field.values()).sorted().forEach(field -> {
            fieldMap.put(field.getColumnName(), field);
        });
    }


    public SalaryListSortInfo(List<SalaryListSortInfo.Field> fields, boolean ascending) {
        super(ascending, fields, SalaryListSortInfo.Field.START_DATE);
    }


    public static SalaryListSortInfo fromPage(Page<?> page) {
        boolean ascending = true;
        if (!page.getSort().toList().isEmpty()) {
            ascending = page.getSort().toList().getFirst().getDirection().isAscending();
        } else {
            return null;
        }

        List<SalaryListSortInfo.Field> sortFields = page.getSort().get().map(order -> {
            return fieldMap.get(order.getProperty());
        }).filter(Objects::nonNull).toList();


        if (sortFields.isEmpty()) {
            return new SalaryListSortInfo(null, ascending);
        }

        return new SalaryListSortInfo(sortFields, ascending);
    }


    public enum Field implements IField {
        START_DATE("startDate"),
        END_DATE("endDate"),
        AMOUNT("amount");
        @Getter
        private final String columnName;
        Field(String columnName) {
            this.columnName = columnName;
        }
    }
}
