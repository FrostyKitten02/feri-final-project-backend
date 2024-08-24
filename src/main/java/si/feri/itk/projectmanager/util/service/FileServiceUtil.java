package si.feri.itk.projectmanager.util.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import si.feri.itk.projectmanager.exceptions.implementation.ItemNotFoundException;
import si.feri.itk.projectmanager.model.project.ProjectFile;

import java.io.File;
import java.nio.file.Path;

@Slf4j
public class FileServiceUtil {

    public static boolean deleteProjectFile(ProjectFile projectFile, Path rootFolder) {
        Resource resource = getProjectFileResource(projectFile, rootFolder);
        try {
            File file = resource.getFile();
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    log.error("Failed to delete file {}", file.getAbsoluteFile());
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

    public static Resource getProjectFileResource(@Valid @NotNull ProjectFile projectFile, Path rootFolder) {
        try {
            //TODO in futer we will have resolve function but for now it is ok
            Path file = rootFolder.resolve(projectFile.getStoredFilePath());
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

    public static String getUploadedFileExtension(MultipartFile file) {
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
