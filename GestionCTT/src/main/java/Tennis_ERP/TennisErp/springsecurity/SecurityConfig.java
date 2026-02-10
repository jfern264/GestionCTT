package Tennis_ERP.TennisErp.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para facilitar pruebas (activar en producción real)
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Permite frames para consola H2 si se usa
                .authorizeHttpRequests(auth -> auth
                        // 1. RECURSOS PÚBLICOS (Vital para que el Login se vea bien)
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/uploads/**", 
                                       "/layout/**", "/webjars/**", "/403", "/404", "/error").permitAll()

                        // 2. RUTAS COMUNES (Acceso para todos los usuarios registrados)
                        .requestMatchers("/menu_principal", "/redireccion", "/perfil/**").authenticated()
                        .requestMatchers("/calendario", "/equipos", "/competicion/ligas", "/competicion/equipos")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_JUGADOR", "ROLE_EMPLEADO", "ROL_EMPLEADO")

                        // 3. RUTAS DE GESTIÓN (Solo Staff: Admin y Empleados)
                        .requestMatchers("/menu_admin/**", "/adminpistas/**", "/trabajadores/**")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLEADO", "ROL_EMPLEADO")

                        // 4. ACCIONES CRÍTICAS (Blindaje de Seguridad - Solo ADMIN Supremo)
                        // Bloqueamos explícitamente el borrado y edición para que nadie pueda hacerlo por URL
                        .requestMatchers("/usuarios/eliminar/**", "/usuarios/guardar/**", "/usuarios/actualizar/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/jugadores/eliminar/**", "/jugadores/guardar/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/pistas/eliminar/**", "/pistas/guardar/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/roles/**").hasAuthority("ROLE_ADMIN")

                        // 5. ACCESO GENERAL A MÓDULOS DE GESTIÓN (Lectura)
                        .requestMatchers("/usuarios/**", "/jugadores/**", "/ligas/**", "/categorias/**", "/competicion/**")
                                .hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redireccion", true) // 'true' fuerza la redirección al menú principal
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                );

        return http.build();
    }
}