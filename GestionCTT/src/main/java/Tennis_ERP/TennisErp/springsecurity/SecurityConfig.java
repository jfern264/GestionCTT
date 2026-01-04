package Tennis_ERP.TennisErp.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                                .csrf(csrf -> csrf.disable())
                                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                                .authorizeHttpRequests(auth -> auth
                                                // 1. PUBLICO: Es VITAL añadir "/layout/**" e "/images/**" aquí para que
                                                // el Login los vea
                                                .requestMatchers("/login", "/css/**", "/js/**", "/images/**",
                                                                "/layout/**", "/403", "/404")
                                                .permitAll()

                                                // 2. CONSULTA: Acceso para todos los roles registrados
                                                .requestMatchers("/menu_principal", "/calendario", "/usuarios",
                                                                "/jugadores", "/trabajadores",
                                                                "/equipos", "/competicion/ligas",
                                                                "/competicion/equipos")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_JUGADOR", "ROL_EMPLEADO")

                                                // 3. GESTIÓN: Solo Admin y Empleados autorizados
                                                .requestMatchers("/menu_admin", "/adminpistas/**")
                                                .hasAnyAuthority("ROLE_ADMIN", "ROL_EMPLEADO")

                                                // 4. ALTA Y EDICIÓN CRÍTICA: Estrictamente ROLE_ADMIN
                                                .requestMatchers("/usuarios/**", "/jugadores/**", "/ligas/**",
                                                                "/categorias/**", "/competicion/**")
                                                .hasAuthority("ROLE_ADMIN")

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                // CAMBIO CLAVE: 'true' obliga a ir a /redireccion e ignora intentos
                                                // previos de cargar JPGs
                                                .defaultSuccessUrl("/redireccion", true)
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true) // Limpia la sesión por completo
                                                .deleteCookies("JSESSIONID") // Borra la cookie de rastreo
                                                .permitAll())

                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/403"));

                return http.build();
        }
}