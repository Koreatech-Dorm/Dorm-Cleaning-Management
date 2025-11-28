package com.dormclean.dorm_cleaning_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("기숙사 청소 관리 API")
                        .description("기숙사 청소 인증 및 관리 시스템 API 명세서입니다.")
                        .version("v1.0.0"));
    }
}
