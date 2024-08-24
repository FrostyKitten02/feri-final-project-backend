package si.feri.itk.projectmanager.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import si.feri.itk.projectmanager.dto.model.ProjectFileDto;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.mapper.ProjectFileMapper;
import si.feri.itk.projectmanager.model.BaseModel;
import si.feri.itk.projectmanager.model.project.ProjectFile;
import si.feri.itk.projectmanager.repository.ProjectFileRepo;
import si.feri.itk.projectmanager.repository.ProjectRepo;
import si.feri.itk.projectmanager.util.RequestUtil;
import si.feri.itk.projectmanager.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    private final Path rootUploadFolder;
    private final ProjectRepo projectRepo;
    private final ProjectFileRepo projectFileRepo;

    public List<ProjectFileDto> getAllProjectFiles(@Valid @NotNull UUID projectId, HttpServletRequest servletRequest) {
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("Files not found"));
        List<ProjectFile> projectFiles = projectFileRepo.findAllByProjectId(projectId);
        
        return projectFiles.stream().map(ProjectFileMapper.INSTANCE::toDto).toList();
    }

    public FileService(@Value("${files.upload-root}") String fileRootPath, ProjectFileRepo projectFileRepo, ProjectRepo projectRepo) {
        if (StringUtil.isNullOrEmpty(fileRootPath)) {
            throw new RuntimeException("FAILED TO CREATE FileService BEAN: Missing file root path");
        }

        this.projectRepo = projectRepo;
        this.rootUploadFolder = Paths.get(fileRootPath);
        this.projectFileRepo = projectFileRepo;

        File rootDir = rootUploadFolder.toFile();
        if (!rootDir.exists()) {
            boolean crated = rootDir.mkdirs();
            if (!crated) {
                throw new RuntimeException("FAILED TO CREATE FileService BEAN: Failed to create root directory " + fileRootPath);
            }
        }
    }

    public ProjectFile getProjectFile(@Valid @NotNull UUID projectFileID, HttpServletRequest servletRequest) {
        ProjectFile projectFile = projectFileRepo.findById(projectFileID).orElseThrow(() -> new ItemNotFoundException("File not found"));

        UUID projectId = projectFile.getProjectId();
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("File not found"));
        return projectFile;
    }

    @Transactional
    public void deleteProjectFile(@Valid @NotNull UUID projectFileID, HttpServletRequest servletRequest) {
        ProjectFile projectFile = projectFileRepo.findById(projectFileID).orElseThrow(() -> new ItemNotFoundException("File not found"));

        UUID projectId = projectFile.getProjectId();
        String userId = RequestUtil.getUserIdStrict(servletRequest);
        projectRepo.findByIdAndOwnerId(projectId, userId).orElseThrow(() -> new ItemNotFoundException("File not found"));

        projectFileRepo.delete(projectFile);
        boolean deleteSuccess = deleteProjectFile(projectFile);
        if (!deleteSuccess) {
            throw new InternalServerException("Failed to delete project file " + projectFile.getProjectId());
        }
    }

    public Resource getProjectFileResource(@Valid @NotNull ProjectFile projectFile) {
        try {
            Path file = rootUploadFolder.resolve(projectFile.getStoredFilePath());
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("File not found: {}", file.toFile().getAbsolutePath());
                throw new ItemNotFoundException("File not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
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
            String extension = getUploadedFileExtension(file);
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
            long written = Files.copy(file.getInputStream(), rootUploadFolder.resolve(projectFileName));
        } catch (IOException e) {
            log.error("Error while saving file to server", e);
        } catch (Exception e) {
            log.error("Error while uploadind file, probably something went wrong saving to db");
        }

        return saved;
    }

    private void deleteProjectFiles(List<ProjectFile> files) {
        for (ProjectFile projectFile : files) {
            deleteProjectFile(projectFile);
        }
    }

    private boolean deleteProjectFile(ProjectFile projectFile) {
        Resource resource = getProjectFileResource(projectFile);
        try {
            File file = resource.getFile();
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.error("Failed to delete file {}", file.getAbsoluteFile());
                }

                if (projectFile.getId() != null) {
                    projectFileRepo.delete(projectFile);
                }
                return deleted;
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file {}", projectFile.getStoredFilePath());
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private String getUploadedFileExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null) {
            return null;
        }
        int firstExtension = name.indexOf(".");

        if (firstExtension == -1) {
            return null;
        }

        return name.substring(firstExtension + 1);
    }
}
