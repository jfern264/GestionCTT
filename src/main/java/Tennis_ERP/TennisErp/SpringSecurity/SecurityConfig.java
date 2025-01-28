package Tennis_ERP.TennisErp.SpringSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void autenticacio(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(("/Usuario/**")).hasAnyRole("Admin", "Trabajador", "Usuario")
                        .requestMatchers(("/menu_admin/**")).hasAnyRole("Admin", "Trabajador")
                        .requestMatchers(("/admincalendario/**")).hasAnyRole("Admin", "Trabajador")
                        .requestMatchers(("/adminpista/**")).hasAnyRole("Admin", "Trabajador")
                        .requestMatchers(("/adminjugadores/**")).hasAnyRole("Admin")
                        .requestMatchers(("/menu_principal/**")).hasAnyRole("Admin", "Trabajador", "Usuario")
                        .requestMatchers(("/calendario/**")).hasAnyRole("Admin", "Trabajador", "Usuario")
                        .requestMatchers(("/jugadores/**")).hasAnyRole("Admin", "Trabajador", "Usuario")
                        .requestMatchers(("/trabajadores/**")).hasAnyRole("Admin", "Trabajador", "Usuario")
                        .requestMatchers(("/pistas/**")).hasAnyRole("Admin", "Trabajador", "Usuario")
                        .requestMatchers(("/login/*")).permitAll()
                        .anyRequest().authenticated()
        )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/menu_principal", true)
                .permitAll()
                )
                .exceptionHandling(exception -> exception
                .accessDeniedPage("/errors/error403")
                );

        http.logout(authz -> authz
                .deleteCookies("JSESSIONID") // Elimina la cookie JSESSIONID al cerrar sesión
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Define la URL de logout
        );

        return http.build(); // Construye el objeto SecurityFilterChain con la configuración aplicada
    }
}
