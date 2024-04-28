package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreateWorkPackageRequest;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.model.WorkPackage;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.repository.WorkPackageRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkPackageService {
    private final WorkPackageRepo workPackageRepo;
    private final ProjectRepo projectRepo;
    public void crateWorkPackage(HttpServletRequest servletRequest, CreateWorkPackageRequest request) {
        String userId = RequestUtil.getUserId(servletRequest);
        if (StringUtil.isNullOrEmpty(userId)) {
            log.warn("Unauthorized user tried to create a project");
            //this should never happen, we have a big problem if it does!
            throw new BadRequestException("User is not logged in");
        }

        if (request.getProjectId() == null) {
            throw new BadRequestException("ProjectId is required");
        }

        Optional<Project> project = projectRepo.findByIdAndOwnerId(request.getProjectId(), userId);

        if (project.isEmpty()) {
            throw new ItemNotFoundException("Project not found");
        }

        if (StringUtil.isNullOrEmpty(request.getTitle())) {
            throw new BadRequestException("Title is required");
        }

        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new BadRequestException("End date is required");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }

        if (request.getIsRelevant() == null) {
            throw new BadRequestException("IsRelevant is required");
        }

        WorkPackage workPackage = new WorkPackage();
        workPackage.setTitle(request.getTitle());
        workPackage.setStartDate(request.getStartDate());
        workPackage.setIsRelevant(request.getIsRelevant());
        workPackage.setEndDate(request.getEndDate());
        workPackage.setProjectId(project.get().getId());
        workPackageRepo.save(workPackage);
    }
}
