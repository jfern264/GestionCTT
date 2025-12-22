package Tennis_ERP.TennisErp.springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    // A partir de Spring Security 5.7+, ya no se recomienda sobrescribir
    // directamente AuthenticationManagerBuilder.
    // En su lugar, se utiliza AuthenticationConfiguration, y Spring se encarga
    // automáticamente de configurar
    // el AuthenticationManager si detecta un UserDetailsService y un
    // PasswordEncoder en el contexto.
    //
    // ¿Por qué funciona sin configurarlo manualmente?
    // Porque Spring Boot detecta que existe un @Bean de PasswordEncoder y un
    // @Service("userDetailsService")
    // que implementa UserDetailsService, y los registra automáticamente para el
    // AuthenticationManager.

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
                        // 1. PUBLICO: Todo el mundo ve el login y los recursos estáticos
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/redireccion").permitAll()

                        // 2. COMPARTIDO: El lugar donde pones lo que el Jugador SI puede ver
                        // Asegúrate de incluir aquí el "menu_principal" y la ruta de "redireccion"
                        .requestMatchers("/menu_principal", "/equipos", "/jugadores")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_JUGADOR")

                        // 3. RESTRINGIDO: Solo para el Admin
                        .requestMatchers("/menu_admin/**").hasAuthority("ROLE_ADMIN")

                        // 4. RESTO: Cualquier otra cosa pide estar logueado
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        // Importante: false para que no fuerce al jugador a ir a la zona de admin
                        .defaultSuccessUrl("/redireccion", false)
                        .permitAll())

                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/errors/error403"));

        return http.build();
    }
}
