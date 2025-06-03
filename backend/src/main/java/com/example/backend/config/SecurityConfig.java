package com.example.backend.config;

import com.example.backend.security.AuthEntryPointJwt;
import com.example.backend.security.AuthTokenFilter;
import com.example.backend.security.JwtUtils;
import com.example.backend.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfig(AuthEntryPointJwt unauthorizedHandler,
                          CustomUserDetailsService userDetailsService,
                          JwtUtils jwtUtils) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      
        // 👉 CREA el filtro aquí, con las dependencias que ya tienes
        AuthTokenFilter authTokenFilter = new AuthTokenFilter(jwtUtils, userDetailsService);
    
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/vinilos/**").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                //Foro
                .requestMatchers(HttpMethod.GET, "/api/foro").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.GET, "/api/foro/**").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.POST, "/api/temas-foro").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.GET, "/api/direcciones-envio/**").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .anyRequest().authenticated()
            )
            // 🔥 INSERTAMOS el filtro ANTES del UsernamePasswordAuthenticationFilter
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
