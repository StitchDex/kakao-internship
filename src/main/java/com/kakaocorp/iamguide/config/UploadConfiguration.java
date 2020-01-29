/*

package com.kakaocorp.iamguide.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class UploadConfiguration implements WebMvcConfigurer {
    @Value("${tenth.host}")
    private String tenthHost;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/iam_user_guide/guide/2020_01/**")
                .addResourceLocations("http://"+tenthHost+"/iam_user_guide/guide/")
                .setCachePeriod(20);
    }
}

*/
