package si.feri.itk.projectmanager.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersonTypeListSortInfo extends SortInfo<PersonTypeListSortInfo.Field> {
    @Getter
    @JsonIgnore
    private static final Map<String, PersonTypeListSortInfo.Field> fieldMap = new HashMap<>();

    static {
        Arrays.stream(PersonTypeListSortInfo.Field.values()).sorted().forEach(field -> {
            fieldMap.put(field.getColumnName(), field);
        });
    }

    public PersonTypeListSortInfo(List<Field> fields, boolean ascending) {
        super(ascending, fields, Field.START_DATE);
    }

    public static PersonTypeListSortInfo fromPage(Page<?> page) {
        boolean ascending = true;
        if (!page.getSort().toList().isEmpty()) {
            ascending = page.getSort().toList().getFirst().getDirection().isAscending();
        } else {
            return null;
        }

        List<PersonTypeListSortInfo.Field> sortFields = page.getSort().get().map(order -> {
            return fieldMap.get(order.getProperty());
        }).filter(Objects::nonNull).toList();


        if (sortFields.isEmpty()) {
            return new PersonTypeListSortInfo(null, ascending);
        }

        return new PersonTypeListSortInfo(sortFields, ascending);
    }

    public enum Field implements IField {
        START_DATE("startDate"),
        END_DATE("endDate"),
        MAX_AVALIBILITY("maxAvailability");
        @Getter
        private final String columnName;
        Field(String columnName) {
            this.columnName = columnName;
        }
    }
}
