package com.cenaflixpodcast.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Handler para uploads de podcasts
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
        
        // Handler para arquivos estáticos (CSS, JS, imagens)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        
    }
}
