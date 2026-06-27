package br.cefetmg.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/books/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAnyRole("ADMIN", "LIBRARIAN")

                .requestMatchers(HttpMethod.GET, "/api/v1/authors/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/authors/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/authors/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/authors/**").hasAnyRole("ADMIN", "LIBRARIAN")

                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
