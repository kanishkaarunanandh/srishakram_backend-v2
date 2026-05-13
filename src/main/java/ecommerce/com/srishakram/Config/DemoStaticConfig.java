package ecommerce.com.srishakram.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DemoStaticConfig implements WebMvcConfigurer {

    @Value("${demo.media.source-dir:../srishakrm images}")
    private String mediaSourceDir;

    @Value("${demo.upload.dir:demo-uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/demo-media/**")
                .addResourceLocations(toLocation(mediaSourceDir));

        Path uploads = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploads);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create demo upload directory", e);
        }

        registry.addResourceHandler("/demo-uploads/**")
                .addResourceLocations(uploads.toUri().toString());
    }

    private String toLocation(String path) {
        return Paths.get(path).toAbsolutePath().normalize().toUri().toString();
    }
}
