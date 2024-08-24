package si.feri.itk.projectmanager.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import si.feri.itk.projectmanager.util.StringUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Component
public class FileUploadConfig {
    private final Path rootUploadFolder;

    public FileUploadConfig(@Value("${files.upload-root}") String fileRootPath) {
        if (StringUtil.isNullOrEmpty(fileRootPath)) {
            throw new RuntimeException("FAILED TO CREATE FileUploadConfig BEAN: Missing file root path");
        }

        this.rootUploadFolder = Paths.get(fileRootPath);

        File rootDir = rootUploadFolder.toFile();
        if (!rootDir.exists()) {
            boolean crated = rootDir.mkdirs();
            if (!crated) {
                throw new RuntimeException("FAILED TO CREATE FileUploadConfig BEAN: Failed to create root directory " + fileRootPath);
            }
        }
    }
}
