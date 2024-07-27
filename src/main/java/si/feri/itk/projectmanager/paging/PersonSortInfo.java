package si.feri.itk.projectmanager.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersonSortInfo extends SortInfo<PersonSortInfo.Field> {
    @Getter
    @JsonIgnore
    private static final Map<String, PersonSortInfo.Field> fieldMap = new HashMap<>();

    static {
        Arrays.stream(PersonSortInfo.Field.values()).sorted().forEach(field -> {
            fieldMap.put(field.getColumnName(), field);
        });
    }

    public PersonSortInfo(List<Field> fields, boolean ascending) {
        super(ascending, fields, Field.NAME);
    }

    public static PersonSortInfo fromPage(Page<?> page) {
        boolean ascending = true;
        if (!page.getSort().toList().isEmpty()) {
            ascending = page.getSort().toList().getFirst().getDirection().isAscending();
        } else {
            return null;
        }

        List<PersonSortInfo.Field> sortFields = page.getSort().get().map(order -> {
            return fieldMap.get(order.getProperty());
        }).filter(Objects::nonNull).toList();


        if (sortFields.isEmpty()) {
            return new PersonSortInfo(null, ascending);
        }

        return new PersonSortInfo(sortFields, ascending);
    }



    public enum Field implements IField {
        NAME("name"),
        LASTNAME("lastname"),
        EMAIL("email");
        @Getter
        private final String columnName;
        Field(String columnName) {
            this.columnName = columnName;
        }
    }
}
