package ecommerce.com.srishakram.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadProductService {

    private final UploadImageService uploadImageService;

    public UploadProductService(UploadImageService uploadImageService) {
        this.uploadImageService = uploadImageService;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        return uploadImageService.uploadFile(file);
    }

    public List<String> uploadMultipleFiles(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFile(file));
        }
        return urls;
    }

    public byte[] downloadFile(String key) throws IOException {
        return uploadImageService.downloadFile(key);
    }
}
