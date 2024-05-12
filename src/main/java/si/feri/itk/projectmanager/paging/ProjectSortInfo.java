package si.feri.itk.projectmanager.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.domain.Page;
import si.feri.itk.projectmanager.model.Project;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ProjectSortInfo extends SortInfo<ProjectSortInfo.Field> {
    @Getter
    @JsonIgnore
    private static final Map<String, Field> fieldMap = new HashMap<>();

    static {
        Arrays.stream(Field.values()).sorted().forEach(field -> {
            fieldMap.put(field.getColumnName(), field);
        });
    }

    public ProjectSortInfo(List<Field> fields, boolean ascending) {
        super(ascending, fields, Field.TITLE);
    }

    public ProjectSortInfo() {
        super(true, null, Field.TITLE);
    }

    public static ProjectSortInfo fromPage(Page<?> page) {
        boolean ascending = true;
        if (!page.getSort().toList().isEmpty()) {
            ascending = page.getSort().toList().getFirst().getDirection().isAscending();
        } else {
            return null;
        }

        List<Field> sortFields = page.getSort().get().map(order -> {
            return fieldMap.get(order.getProperty());
        }).filter(Objects::nonNull).toList();


        if (sortFields.isEmpty()) {
            return new ProjectSortInfo(null, ascending);
        }

        return new ProjectSortInfo(sortFields, ascending);
    }

    public enum Field implements IField {
        CREATED_AT("createdAt"),
        TITLE("title");
        @Getter
        private final String columnName;
        Field(String columnName) {
            this.columnName = columnName;
        }
    }

}
