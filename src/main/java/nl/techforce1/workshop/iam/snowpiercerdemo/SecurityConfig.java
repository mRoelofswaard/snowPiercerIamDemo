package nl.techforce1.workshop.iam.snowpiercerdemo;

import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.DIRECTOR;
import static nl.techforce1.workshop.iam.snowpiercerdemo.domain.Role.ENGINEER;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    SecurityConfig(final KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST)
                        .hasRole(DIRECTOR.name())
                        .requestMatchers("snowpiercer/cars/engine/*")
                        .hasRole(ENGINEER.name())
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(authConfigurer -> authConfigurer.jwt().jwkSetUri("http://localhost:8380/realms/snowpiercer/protocol/openid-connect/certs"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults());

        return http.build();
    }

}