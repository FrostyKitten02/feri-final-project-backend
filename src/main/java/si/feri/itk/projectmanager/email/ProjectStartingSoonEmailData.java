package si.feri.itk.projectmanager.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.datetime.DateFormatter;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.project.ProjectMinimal;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProjectStartingSoonEmailData {
    private ProjectMinimal project;
    private Person owner;
    private String url;

    public Map<String, String> getTemplateValues() {
        Map<String, String> values = new HashMap<>();
        values.put("project.title", project.getTitle());
        values.put("project.link", url);
        values.put("project.owner.full-name", getOwnerFullName());
        values.put("project.start-date", project.getStartDate().format(DateTimeFormatter.ISO_DATE));
        return values;
    }

    //TODO extract to util class, use interface on person and personDto
    private String getOwnerFullName() {
        if (owner.getName() != null && owner.getLastname() != null) {
            return owner.getName() + " " + owner.getLastname();
        }

        if (owner.getName() == null && owner.getLastname() != null) {
            return owner.getLastname();
        }

        if (owner.getName() != null && owner.getLastname() == null) {
            return owner.getName();
        }

        return "";
    }
}
