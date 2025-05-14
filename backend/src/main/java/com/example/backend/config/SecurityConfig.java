package com.example.backend.config;

import com.example.backend.security.AuthEntryPointJwt;
import com.example.backend.security.AuthTokenFilter;
import com.example.backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final AuthEntryPointJwt        unauthorizedHandler;
    private final AuthTokenFilter          authTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .cors(cors -> {})
          .csrf(csrf -> csrf.disable())
          .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
          .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(auth -> auth
              // Endpoints públicos
              .requestMatchers("/api/auth/**").permitAll()
              .requestMatchers(HttpMethod.GET,
                  "/api/eventos/**",
                  "/api/vinilos/**",
                  "/api/foros/**",
                  "/api/temas-foro/**",
                  "/api/comentarios-foro/**"
              ).permitAll()

              // Eventos
              .requestMatchers("/api/eventos/**")
                .hasAnyRole("ADMINISTRADOR","TRABAJADOR")

              // Asistencias a eventos
              .requestMatchers(HttpMethod.POST,   "/api/asistencias-evento/**")
                .hasRole("CLIENTE")
              .requestMatchers("/api/asistencias-evento/**")
                .hasAnyRole("ADMINISTRADOR","MODERADOR","TRABAJADOR")

              // Direcciones de envío
              .requestMatchers("/api/direcciones-envio/**")
                .hasAnyRole("CLIENTE","ADMINISTRADOR")

              // Pedidos
              .requestMatchers(HttpMethod.GET,  "/api/pedidos/**")
                .hasAnyRole("ADMINISTRADOR","TRABAJADOR","CLIENTE")
              .requestMatchers(HttpMethod.POST, "/api/pedidos/**")
                .hasRole("CLIENTE")
              .requestMatchers("/api/pedidos/**")
                .hasRole("ADMINISTRADOR")

              // Proveedores y vinilos
              .requestMatchers(
                  "/api/proveedores/**",
                  "/api/vinilos/**"
              ).hasAnyRole("ADMINISTRADOR","TRABAJADOR")

              // Usuarios
              .requestMatchers("/api/usuarios/**")
                .hasRole("ADMINISTRADOR")

              // Foros y temas
              .requestMatchers(HttpMethod.POST,
                  "/api/foros/**",
                  "/api/temas-foro/**"
              ).hasAnyRole("CLIENTE","MODERADOR","ADMINISTRADOR")
              .requestMatchers(HttpMethod.PUT,
                  "/api/foros/**",
                  "/api/temas-foro/**"
              ).hasAnyRole("MODERADOR","ADMINISTRADOR")
              .requestMatchers(HttpMethod.DELETE,
                  "/api/foros/**",
                  "/api/temas-foro/**"
              ).hasAnyRole("MODERADOR","ADMINISTRADOR")

              // Bloqueos de foro
              .requestMatchers("/api/bloqueos-foro/**")
                .hasAnyRole("MODERADOR","ADMINISTRADOR")

              // Comentarios de foro
              .requestMatchers(HttpMethod.POST,   "/api/comentarios-foro/**")
                .hasRole("CLIENTE")
              .requestMatchers(HttpMethod.PUT,    "/api/comentarios-foro/**")
                .hasAnyRole("MODERADOR","ADMINISTRADOR")
              .requestMatchers(HttpMethod.DELETE, "/api/comentarios-foro/**")
                .hasAnyRole("MODERADOR","ADMINISTRADOR")

              // Cualquier otra ruta requiere autenticación
              .anyRequest().authenticated()
          )
          // Especifica el UserDetailsService para la autenticación
          .userDetailsService(userDetailsService);

        // Filtro JWT antes de procesar autenticación de usuario/contraseña
        http.addFilterBefore(authTokenFilter,
            UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
