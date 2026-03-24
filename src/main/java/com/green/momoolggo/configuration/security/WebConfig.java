package com.green.momoolggo.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.menu-path}")
    private String menuUploadPath;

    @Value("${file.upload.store-path}")
    private String storeUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/menu/**")
                .addResourceLocations("file:" + menuUploadPath);

        registry.addResourceHandler("/uploads/store/**")
                .addResourceLocations("file:" + storeUploadPath);
        // 이거 수정된건지 확인용

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = location.createRelative(resourcePath);

                        if(resource.exists() && resource.isReadable()) {
                            return resource;
                        }

                        return new ClassPathResource("/static/index.html");
                    }
                });
    }
}