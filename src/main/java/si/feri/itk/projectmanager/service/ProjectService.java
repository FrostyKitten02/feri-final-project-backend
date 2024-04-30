package si.feri.itk.projectmanager.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import si.feri.itk.projectmanager.dto.request.CreateProjectRequest;
import si.feri.itk.projectmanager.dto.model.ProjectDto;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.ProjectMapper;
import si.feri.itk.projectmanager.model.Project;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepo projectRepo;
    public UUID createProject(CreateProjectRequest request, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserId(servletRequest);
        if (StringUtil.isNullOrEmpty(userId)) {
            log.warn("Unauthorized user tried to create a project");
            //this should never happen, we have a big problem if it does!
            throw new BadRequestException("User is not logged in");
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


        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setOwnerId(userId);
        return projectRepo.save(project).getId();
    }

    public ProjectDto getProjectById(UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserId(servletRequest);
        if (StringUtil.isNullOrEmpty(userId)) {
            log.warn("Unauthorized user tried to get a project");
            //this should never happen, we have a big problem if it does!
            throw new BadRequestException("User is not logged in");
        }

        //we may not want to check if owner made the call but for now its ok, when we allow other users to see projects we have to check if they can see it!!!
        Optional<Project> project = projectRepo.findByIdAndOwnerId(projectId, userId);
        return project.map(ProjectMapper.INSTANCE::toDto).orElseThrow( () -> new ItemNotFoundException("Project not found"));
    }

}
