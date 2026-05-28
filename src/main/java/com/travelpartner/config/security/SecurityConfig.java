package com.travelpartner.config.security;

import com.travelpartner.config.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CORS Configuration Added
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
//                    config.setAllowedOrigins(List.of(
//                            "https://ops.traveling-partner.com",
//                            "https://traveling-partner.com",
//                            "http://localhost:3000",        // React Web
//                            "http://localhost:5173",        // Vite React
//                            "http://localhost:8081",        // React Native Metro
//                            "http://10.0.2.2:8081",         // Android Emulator
//                            "http://192.168.0.0/16",        // Local Network Devices
//                            "https://your-production.com"   // Production domain
//                    ));
                    config.setAllowedOriginPatterns(List.of("*"));

                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

                    config.setAllowedHeaders(List.of("*"));

                    config.setAllowCredentials(true);
                    return config;
                }))

                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/health",
                                "/healthCheck/**",
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/documents/**",
                                "/api/contact/submit",
                                "/api/banners/carousel",
                                "/api/website/blog/list",
                                "/api/website/blog/view/{id}",
                                "/api/website/newsletter/list",
                                "/api/website/newsletter/view/**",
                                "/ws/chat",
                                "/ws/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(rateLimitFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}