package dev.Proyect.Ucompensar.EasyTask.WebSecurity;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // Permitir acceso a Swagger
                        .requestMatchers("/api/**").permitAll() // Permitir acceso a todas las rutas de API sin autenticación
                        .anyRequest().permitAll() // Cualquier otra solicitud requiere autenticación
                )
                .csrf(csrf -> csrf.disable()); // Desactivar CSRF

        return http.build();
    }
}
