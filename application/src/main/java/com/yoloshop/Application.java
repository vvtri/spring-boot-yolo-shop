package com.yoloshop;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.type(SecurityScheme.Type.HTTP);
        securityScheme.in(SecurityScheme.In.HEADER);
        securityScheme.scheme("bearer")        ;
        securityScheme.bearerFormat("Bearer ")     ;
        return new OpenAPI().components(new Components()
                .addSecuritySchemes("bearer", securityScheme));
    }

    

}
