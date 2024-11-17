package ru.zmaev.managment.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@RequiredArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AuthConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserInfo userInfo() {
        return new UserInfo(
                AuthUtils.getCurrentUserId(),
                AuthUtils.getJwt(),
                AuthUtils.getUsername(),
                AuthUtils.getEmail(),
                AuthUtils.getCurrentRoles()
        );
    }
}
