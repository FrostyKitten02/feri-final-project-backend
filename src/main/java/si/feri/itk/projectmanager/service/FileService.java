package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.feri.itk.projectmanager.configuration.FileUploadConfig;
import si.feri.itk.projectmanager.dto.model.ProjectFileDto;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.ProjectFileMapper;
import si.feri.itk.projectmanager.model.BaseModel;
import si.feri.itk.projectmanager.model.project.ProjectFile;
import si.feri.itk.projectmanager.repository.ProjectFileRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.service.FileServiceUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileUploadConfig uploadConfig;
    private final ProjectRepo projectRepo;
    private final ProjectFileRepo projectFileRepo;
    private final FileUploadConfig fileUploadConfig;

    public List<ProjectFileDto> getAllProjectFiles(@Valid @NotNull UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Files not found"));
        List<ProjectFile> projectFiles = projectFileRepo.findAllByProjectId(projectId);
        
        return projectFiles.stream().map(ProjectFileMapper.INSTANCE::toDto).toList();
    }


    public ProjectFile getProjectFile(@Valid @NotNull UUID projectFileID, HttpServletRequest servletRequest) {
        ProjectFile projectFile = projectFileRepo.findById(projectFileID).orElseThrow(() -> new ItemNotFoundException("File not found"));

        UUID projectId = projectFile.getProjectId();
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("File not found"));
        return projectFile;
    }

    public Resource getProjectFileResource(ProjectFile projectFile) {
        Resource resource = FileServiceUtil.getProjectFileResource(projectFile, uploadConfig.getRootUploadFolder());
        if (resource == null) {
            throw new ItemNotFoundException("File not found");
        }
        return resource;
    }

    @Transactional
    public void deleteProjectFile(@Valid @NotNull UUID projectFileID, HttpServletRequest servletRequest) {
        ProjectFile projectFile = projectFileRepo.findById(projectFileID).orElseThrow(() -> new ItemNotFoundException("File not found"));

        UUID projectId = projectFile.getProjectId();
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("File not found"));

        projectFileRepo.delete(projectFile);
        boolean deleteSuccess = FileServiceUtil.deleteProjectFile(projectFile, uploadConfig.getRootUploadFolder());
        if (!deleteSuccess) {
            throw new InternalServerException("Failed to delete project file " + projectFile.getProjectId());
        } else {
            projectFileRepo.delete(projectFile);
        }
    }

    @Transactional
    public List<UUID> uploadProjectFiles(MultipartFile[] files, @Valid @NotNull UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Project not found"));

        List<ProjectFile> saved = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            ProjectFile savedFile = uploadProjectFile(file, projectId);
            if (savedFile == null) {
                //this is the inner function mentioned above
                deleteProjectFiles(saved);
                throw new InternalServerException("Error while saving files");
            }

            saved.add(savedFile);
        }

        return saved.stream().map(BaseModel::getId).toList();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected ProjectFile uploadProjectFile(MultipartFile file, UUID projectId) {
        ProjectFile saved = null;
        try {
            String extension = FileServiceUtil.getUploadedFileExtension(file);
            String projectFileName = UUID.randomUUID() + "." + extension;
            ProjectFile projectFile = new ProjectFile();
            projectFile.setOriginalFileName(file.getOriginalFilename());
            projectFile.setStoredFilePath(projectFileName);
            projectFile.setProjectId(projectId);
            projectFile.setContentType(file.getContentType());
            int sizeMB = file.getBytes().length / 1_000_000;
            projectFile.setFileSizeMB(sizeMB);

            //TODO check bytes written!!!
            saved = projectFileRepo.save(projectFile);
            long written = Files.copy(file.getInputStream(), fileUploadConfig.getRootUploadFolder().resolve(projectFileName));
        } catch (IOException e) {
            log.error("Error while saving file to server", e);
        } catch (Exception e) {
            log.error("Error while uploadind file, probably something went wrong saving to db");
        }

        return saved;
    }

    private void deleteProjectFiles(List<ProjectFile> files) {
        for (ProjectFile projectFile : files) {
            boolean deleted = FileServiceUtil.deleteProjectFile(projectFile, uploadConfig.getRootUploadFolder());
            if (deleted) {
                projectFileRepo.delete(projectFile);
            }
        }
    }

}
