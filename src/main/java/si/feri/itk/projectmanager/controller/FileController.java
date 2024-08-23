package si.feri.itk.projectmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import si.feri.itk.projectmanager.dto.response.ResourceCreatedResponse;
import si.feri.itk.projectmanager.service.FileService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping(
            value="project/{projectId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResourceCreatedResponse uploadProjectFile(@RequestParam("files") MultipartFile[] files, @PathVariable UUID projectId, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        List<UUID> created = fileService.uploadProjectFiles(files, projectId);
        servletResponse.setStatus(HttpServletResponse.SC_CREATED);

        ResourceCreatedResponse response = new ResourceCreatedResponse();
        response.setIds(created);
        return response;
    }

    @GetMapping( "/download/{projectFileId}")
    public ResponseEntity<Resource> download(@PathVariable UUID projectFileId) throws FileNotFoundException {
        Resource resource = fileService.downloadProjectFile(projectFileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
