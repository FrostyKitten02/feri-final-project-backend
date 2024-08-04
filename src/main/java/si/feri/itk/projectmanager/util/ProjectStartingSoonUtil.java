package si.feri.itk.projectmanager.util;

import si.feri.itk.projectmanager.dto.request.project.UpdateProjectRequest;
import si.feri.itk.projectmanager.model.project.Project;
import si.feri.itk.projectmanager.model.ProjectStartingSoonEmailQueue;

import java.time.LocalDate;

public class ProjectStartingSoonUtil {
    private ProjectStartingSoonUtil() {
    }

    private static final int MONTHS_BEFORE_START = 1;

    /**
     * Purpose of this function is to determine if there is any point in creating or updating project starting soon email queue.
     * If project start is in the past, there is no point in creating or updating project starting soon email queue, because project has already started.
     * This should be called before creating or updating project starting soon email queue
     *
     * @param project new or updated project
     * @return true only if project start is not in the past
     */
    public static boolean shouldCreateOrUpdateProjectStartingSoonEmailQueue(Project project) {
        LocalDate now = LocalDate.now();
        boolean currentStartDateIsBeforeNow = !project.getStartDate().isAfter(now);
        if (currentStartDateIsBeforeNow) {
            return false;
        }

        return true;
    }

    public static ProjectStartingSoonEmailQueue createProjectStartingSoonEmailQueue(Project project) {
        ProjectStartingSoonEmailQueue projectStartingSoonEmailQueue = new ProjectStartingSoonEmailQueue();
        projectStartingSoonEmailQueue.setProjectId(project.getId());
        projectStartingSoonEmailQueue.setSendAt(project.getStartDate().minusMonths(MONTHS_BEFORE_START));
        projectStartingSoonEmailQueue.setAttempts(0);
        projectStartingSoonEmailQueue.setSent(false);
        return projectStartingSoonEmailQueue;
    }

    public static void updateProjectStartingSoonEmailQueue(ProjectStartingSoonEmailQueue currentEmail, Project project, LocalDate prevStartDate) {
        if (project.getStartDate().isEqual(prevStartDate)) {
            return;
        }

        LocalDate now = LocalDate.now();
        boolean currentStartIsBeforeNow = !project.getStartDate().isAfter(now);
        if (currentStartIsBeforeNow) {
            //if both dates are in the past, we don't want to send any more emails, so we just mark it as sent
            currentEmail.setSent(true);
            return;
        }

        currentEmail.setSendAt(project.getStartDate().minusMonths(MONTHS_BEFORE_START));
        currentEmail.setAttempts(0);
        currentEmail.setSent(false);
    }
}