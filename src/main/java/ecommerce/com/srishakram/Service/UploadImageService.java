package ecommerce.com.srishakram.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadImageService {

    private final Path uploadDir;

    public UploadImageService(@Value("${demo.upload.dir:demo-uploads}") String uploadDir) throws IOException {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = sanitize(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + originalName;
        Path target = uploadDir.resolve(fileName).normalize();

        if (!target.startsWith(uploadDir)) {
            throw new IOException("Invalid upload path");
        }

        Files.copy(file.getInputStream(), target);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/demo-uploads/")
                .path(fileName)
                .toUriString();
    }

    public byte[] downloadFile(String keyOrUrl) throws IOException {
        String fileName = extractFileName(keyOrUrl);
        Path target = uploadDir.resolve(fileName).normalize();
        if (!target.startsWith(uploadDir) || !Files.exists(target)) {
            throw new IOException("File not found");
        }
        return Files.readAllBytes(target);
    }

    private String sanitize(String name) {
        String fallback = "upload";
        String clean = name == null || name.isBlank() ? fallback : name;
        return clean.replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "");
    }

    private String extractFileName(String keyOrUrl) {
        try {
            return Paths.get(new URI(keyOrUrl).getPath()).getFileName().toString();
        } catch (IllegalArgumentException | URISyntaxException e) {
            return Paths.get(keyOrUrl).getFileName().toString();
        }
    }
}
