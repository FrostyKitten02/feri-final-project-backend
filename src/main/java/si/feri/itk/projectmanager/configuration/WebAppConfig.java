package si.feri.itk.projectmanager.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
public class WebAppConfig {

    @Value("${web.app.base-url}")
    private String webAppBasePath;

    @Value("${web.app.project-path}")
    private String webAppProjectPath;

    public String getProjectUrl(UUID projectId) {
        return webAppBasePath + webAppProjectPath.replace("{{projectId}}", projectId.toString());
    }
}
