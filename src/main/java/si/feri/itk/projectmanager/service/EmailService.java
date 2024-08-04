package si.feri.itk.projectmanager.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.email.ProjectStartingSoonEmailData;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateResolverService emailTemplateResolverService;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.templates.project-starting-soon}")
    private String projectStartingSoonTemplate;

    public void createAndSendProjectStartingSoonEmail(ProjectStartingSoonEmailData data) throws IOException {
        MimeMessage message = createProjectStartingSoonEmail(data);
        sendEmail(message);
    }

    private MimeMessage createProjectStartingSoonEmail(ProjectStartingSoonEmailData data) throws IOException {
        String to = data.getOwner().getEmail();
        String subject = "Project starting soon!";
        String template = emailTemplateResolverService.resolveAndFillTemplate(projectStartingSoonTemplate, data.getTemplateValues());
        return createMimeMessage(to, subject, template);
    }

    private MimeMessage createMimeMessage(String to, String subject, String template) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(template, "text/html");
            return message;
        } catch (Exception e) {
            log.error("Failed to create email message!");
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    private void sendEmail(MimeMessage ...message) {
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email!");
            log.error(e.getLocalizedMessage(), e);
        }
    }

}
