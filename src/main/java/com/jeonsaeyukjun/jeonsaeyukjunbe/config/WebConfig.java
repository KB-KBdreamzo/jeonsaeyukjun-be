package com.jeonsaeyukjun.jeonsaeyukjunbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.jeonsaeyukjun.jeonsaeyukjunbe")
public class WebConfig implements WebMvcConfigurer {

    String fileUploadPath="";
    public WebConfig(){
        System.out.println("WebConfig created");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    // 오류시, 버전맞게 수정
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //CORS정책오류 해결
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "get", "post"); //대소문자 다 써야 함
    }

}