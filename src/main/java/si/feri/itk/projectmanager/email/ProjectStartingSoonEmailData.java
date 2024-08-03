package si.feri.itk.projectmanager.email;

import lombok.Getter;
import lombok.Setter;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.person.Person;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProjectStartingSoonEmailData {
    private Project project;
    private Person owner;

    public Map<String, String> getTemplateValues() {
        Map<String, String> values = new HashMap<>();
        values.put("project.title", project.getTitle());
        values.put("project.owner.name", owner.getName());
        return values;
    }
}
