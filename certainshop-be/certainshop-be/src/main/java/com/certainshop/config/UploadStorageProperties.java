package com.certainshop.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class UploadStorageProperties {

    private final Path uploadImagesDir;
    private final Path uploadsRootDir;
    // Thư mục legacy (nơi ảnh cũ được lưu trong project, dùng đường dẫn tuyệt đối)
    private final Path legacyUploadsRootDir;

    public UploadStorageProperties(
            @Value("${app.upload.dir:C:/Users/admin/certainshop/uploads/images}") String configuredUploadDir,
            @Value("${app.legacy.upload.dir:}") String legacyUploadDir) {
        this.uploadImagesDir = Paths.get(configuredUploadDir).toAbsolutePath().normalize();
        this.uploadsRootDir = this.uploadImagesDir.getParent();

        // Nếu có cấu hình legacy, dùng nó. Nếu không, thử tự tìm theo working directory.
        if (legacyUploadDir != null && !legacyUploadDir.isEmpty()) {
            this.legacyUploadsRootDir = Paths.get(legacyUploadDir).toAbsolutePath().normalize();
        } else {
            // Fallback: thư mục uploads/ trong cùng thư mục chạy server
            this.legacyUploadsRootDir = Paths.get("uploads").toAbsolutePath().normalize();
        }
    }

    @PostConstruct
    void ensureDirectoriesExist() throws IOException {
        Files.createDirectories(uploadImagesDir);
    }

    public Path getUploadImagesDir() {
        return uploadImagesDir;
    }

    public Path getUploadsRootDir() {
        return uploadsRootDir;
    }

    public Path getLegacyUploadsRootDir() {
        return legacyUploadsRootDir;
    }

    public String toPublicImagePath(String fileName) {
        return "/uploads/images/" + fileName;
    }
}