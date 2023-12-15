package com.example.withdogandcat.global.config.prod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Profile("!Prod")
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWTAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                .schema("LoginRequestDto", new Schema<>()
                        .type("object")
                        .addProperty("email", new StringSchema().example("user@example.com"))
                        .addProperty("password", new StringSchema().example("password123!"))
                )

                // 로그인 수동경로 지정
                .path("/api/user/login", new PathItem()
                        .post(new Operation()
                                .summary("로그인")
                                .description("사용자 로그인을 위한 엔드포인트")
                                .tags(Collections.singletonList("auth"))
                                .operationId("loginUser")
                                .requestBody(new RequestBody()
                                        .content(new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/LoginRequestDto"))))
                                )
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("로그인 성공"))
                                        .addApiResponse("401", new ApiResponse().description("인증 실패"))
                                )));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-auth")
                .pathsToMatch("/api/user/**")
                .build();
    }

    // 네이버 검색 컨트롤러
    @Bean
    public GroupedOpenApi naverApi() {
        return GroupedOpenApi.builder()
                .group("naver-search")
                .pathsToMatch("/open/naver/news")
                .build();
    }

    @Bean
    public GroupedOpenApi shopApi() {
        return GroupedOpenApi.builder()
                .group("shop-crud")
                .pathsToMatch("/api/shops/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reviewApi() {
        return GroupedOpenApi.builder()
                .group("review-crud")
                .pathsToMatch("/api/reviews/**")
                .build();
    }

}
