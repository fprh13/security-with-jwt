package com.daemoing.daemo.global.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger springdoc-ui 구성 파일
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("대모중 그룹 중계 사이트")
                .version("v1.6.14")
                .description("**`대학생` 모임 중계 사이트를 제작합니다.** ");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
