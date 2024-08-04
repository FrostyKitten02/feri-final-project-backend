package si.feri.itk.projectmanager.cron;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import si.feri.itk.projectmanager.configuration.WebAppConfig;
import si.feri.itk.projectmanager.email.ProjectStartingSoonEmailData;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.ProjectStartingSoonEmailQueue;
import si.feri.itk.projectmanager.model.person.Person;
import si.feri.itk.projectmanager.model.project.ProjectMinimal;
import si.feri.itk.projectmanager.repository.PersonRepo;
import si.feri.itk.projectmanager.repository.ProjectMinimalRepo;
import si.feri.itk.projectmanager.repository.ProjectStartingSoonEmailQueueRepo;
import si.feri.itk.projectmanager.service.EmailService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectStartingSoonSender {
    private final PersonRepo personRepo;
    private final EmailService emailService;
    private final WebAppConfig webAppConfig;
    private final ProjectMinimalRepo projectMinimalRepo;
    private final ProjectStartingSoonEmailQueueRepo projectStartingSoonEmailQueueRepo;
    private static final int FAIL_ATTEMPTS_LIMIT = 3;

    //every day at 6:00
    @Transactional
    @Scheduled(cron = "0 0 6 * * *")
    public void sendProjectStartingSoonEmails() {
        StopWatch swTotal = new StopWatch();
        swTotal.start();
        LocalDate today = LocalDate.now();
        List<ProjectStartingSoonEmailQueue> queue = projectStartingSoonEmailQueueRepo.findAllBySendAtBeforeOrEqualAndAttemptsLessThan(today, FAIL_ATTEMPTS_LIMIT);
        Map<UUID, ProjectMinimal> projectMap = getProjectMap(queue);
        Map<String, Person> ownerMap = getOwnerMap(projectMap.values().stream().toList());

        StopWatch emailSending = new StopWatch();
        emailSending.start();
        for (ProjectStartingSoonEmailQueue q : queue) {
            ProjectMinimal project = projectMap.get(q.getProjectId());
            Person owner = ownerMap.get(project.getOwnerId());

            ProjectStartingSoonEmailData emailData = new ProjectStartingSoonEmailData();
            emailData.setProject(project);
            emailData.setOwner(owner);
            emailData.setUrl(webAppConfig.getProjectUrl(project.getId()));

            try {
                emailService.createAndSendProjectStartingSoonEmail(emailData);
                q.setSent(true);
            } catch (Exception e) {
                q.setAttempts(q.getAttempts() + 1);
                q.setSent(false);
                log.error("Failed to send email for project: " + project.getTitle());
                log.error(e.getLocalizedMessage(), e);
            }
        }
        emailSending.stop();
        log.info("Emails sent in: " + emailSending.formatTime());


        try {
            projectStartingSoonEmailQueueRepo.saveAll(queue);
        } catch (Exception e) {
            log.error("Failed to save project starting soon email queue!");
            log.error(e.getLocalizedMessage(), e);
        }

        try {
            projectStartingSoonEmailQueueRepo.deleteAllSent();
        } catch (Exception e) {
            log.error("Failed to delete sent project starting soon emails!");
            log.error(e.getLocalizedMessage(), e);
        }

        swTotal.stop();
        log.info("ProjectStartingSoonSender finished in: " + swTotal.formatTime());
    }

    private Map<UUID, ProjectMinimal> getProjectMap(List<ProjectStartingSoonEmailQueue> queue) {
        List<UUID> projectIds = queue.stream().map(ProjectStartingSoonEmailQueue::getProjectId).toList();
        List<ProjectMinimal> projects = projectMinimalRepo.findAllByIdIn(projectIds);
        return projects.stream().collect(Collectors.toMap(ProjectMinimal::getId, p -> p));
    }


    private Map<String, Person> getOwnerMap(List<ProjectMinimal> projects) {
        List<String> ownerIds = projects.stream().map(p -> p.getOwnerId()).toList();
        List<Person> owners = personRepo.findAllByClerkIdIn(ownerIds);
        return owners.stream().collect(Collectors.toMap(Person::getClerkId, p -> p));
    }

}
