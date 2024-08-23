package si.feri.itk.projectmanager.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.BaseModel;
import si.feri.itk.projectmanager.model.project.ProjectFile;
import si.feri.itk.projectmanager.repository.ProjectFileRepo;
import si.feri.itk.projectmanager.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private final ProjectFileRepo projectFileRepo;

    public FileService(@Value("${files.upload-root}") String fileRootPath, ProjectFileRepo projectFileRepo) {
        if (StringUtil.isNullOrEmpty(fileRootPath)) {
            throw new RuntimeException("FAILED TO CREATE FileService BEAN: Missing file root path");
        }
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

    public Resource downloadProjectFile(UUID projectFileID) throws FileNotFoundException {
        ProjectFile projectFile = projectFileRepo.findById(projectFileID).orElseThrow(() -> new ItemNotFoundException("File not found"));

        try {
            Path file = rootUploadFolder.resolve(projectFile.getStoredFilePath());
            Resource resource = new UrlResource(file.toUri());

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
    public List<UUID> uploadProjectFiles(MultipartFile[] files, @Valid @NotNull UUID projectId) {
        List<ProjectFile> saved = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            ProjectFile savedFile = uploadProjectFile(file, projectId);
            if (savedFile == null) {
                //TODO handle delete all already saved files!!!
                return null;
            }

            saved.add(savedFile);
        }

        return saved.stream().map(BaseModel::getId).toList();
    }

    private ProjectFile uploadProjectFile(MultipartFile file, UUID projectId) {
        try {
            String extension = getUploadedFileExtension(file);
            String projectFileName = UUID.randomUUID() + "." + extension;
            ProjectFile projectFile = new ProjectFile();
            projectFile.setOriginalFileName(file.getOriginalFilename());
            projectFile.setStoredFilePath(projectFileName);
            projectFile.setProjectId(projectId);
            int sizeMB = file.getBytes().length / 1_000_000;
            projectFile.setFileSizeMB(sizeMB);

            //TODO check bytes written!!!
            long written = Files.copy(file.getInputStream(), rootUploadFolder.resolve(projectFileName));
            return projectFileRepo.save(projectFile);
        } catch (IOException e) {
            return null;
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
