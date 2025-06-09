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

/**
 * Configuración principal de seguridad para la aplicación.
 * Define reglas de autorización, gestión de sesiones, CORS, y filtros JWT.
 */
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

    /**
     * Configura el filtro de seguridad HTTP y las reglas de autorización.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthTokenFilter authTokenFilter = new AuthTokenFilter(jwtUtils, userDetailsService);

        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF (no es necesario para API REST)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Permite el acceso a Swagger y OpenAPI
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/swagger-ui.html",
                    "/error"
                ).permitAll()

                // Rutas públicas (login y registro)
                .requestMatchers("/api/auth/**").permitAll()

                // Rutas públicas para visualización de vinilos
                .requestMatchers(HttpMethod.GET, "/api/vinilos/**").permitAll()

                // Rutas protegidas por roles
                .requestMatchers("/api/pedidos/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/api/eventos/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                .requestMatchers("/api/eventos/**")
                    .hasAnyRole("EMPLEADO", "ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/api/usuarios/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**")
                    .hasRole("ADMINISTRADOR")

                .requestMatchers(HttpMethod.GET, "/api/foro/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.POST, "/api/temas-foro")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.POST, "/api/comentarios")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/comentarios/**")
                    .hasAnyRole("MODERADOR", "ADMINISTRADOR")

                .requestMatchers("/api/direcciones-envio/**")
                    .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")

                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura el origen y políticas de CORS permitidas.
     */
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

    /**
     * Bean para la gestión de autenticación con Spring Security.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Codificador de contraseñas usando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
