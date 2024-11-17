package ru.zmaev.managment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private @Value("${spring.application.name}") String applicationName;
    private @Value("${spring.application.version}") String applicationVersion;
    private @Value("${oAuth_security_schema}") String securityScheme;
    private @Value("${keycloak-auth-redirect}") String keycloakAuthRedirect;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(securityScheme, createOAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(securityScheme))
                .info(new Info().title(applicationName).version(applicationVersion));
    }

    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = createOAuthFlows();
        return new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows() {
        OAuthFlow flow = createAuthorizationCodeFlow();
        return new OAuthFlows().implicit(flow);
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        return new OAuthFlow()
                .authorizationUrl(keycloakAuthRedirect);
    }
}
