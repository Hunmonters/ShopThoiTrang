package com.certainshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UploadStorageProperties uploadStorageProperties;

    public WebMvcConfig(UploadStorageProperties uploadStorageProperties) {
        this.uploadStorageProperties = uploadStorageProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Thư mục chính lưu ảnh (C:/Users/admin/certainshop/uploads/)
        // Request /uploads/images/abc.jpg → tìm trong C:/Users/admin/certainshop/uploads/images/abc.jpg
        String uploadRootUri = "file:///" + uploadStorageProperties.getUploadsRootDir()
                .toAbsolutePath().normalize().toString().replace("\\", "/") + "/";

        // Thư mục legacy trong project (certainshop-be/uploads/)
        String legacyUploadRootUri = "file:///" + uploadStorageProperties.getLegacyUploadsRootDir()
                .toAbsolutePath().normalize().toString().replace("\\", "/") + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadRootUri, legacyUploadRootUri);

        // CSS, JS, images tĩnh
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
