package si.feri.itk.projectmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailTemplateResolverService {
    private final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
    @Value("${spring.mail.templates.path}")
    private String templatesPath;
    private static final String TEMPLATE_KEY_PREFIX = "{{";
    private static final String TEMPLATE_KEY_SUFFIX = "}}";

    public String resolveAndFillTemplate(String templateName, Map<String, String> values) throws IOException {
        if (StringUtil.isNullOrEmpty(templateName) || values == null || values.isEmpty()) {
            throw new InternalServerException("Invalid template name or empty values");
        }

        String template = resolveTemplate(templateName);
        return fillTemplate(template, values);
    }

    private String resolveTemplate(String templateName) throws IOException {
        String fullPath = templatesPath + "/" + templateName;
        Resource resource =ctx.getResource(fullPath);
        return new String(Files.readAllBytes(resource.getFile().toPath()));
    }

    private String fillTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String toReplace = TEMPLATE_KEY_PREFIX + entry.getKey() + TEMPLATE_KEY_SUFFIX;
            template = template.replace(toReplace, entry.getValue());
        }
        return template;
    }
}
