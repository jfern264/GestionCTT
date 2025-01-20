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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Configuración del PasswordEncoder (BCryptPasswordEncoder para la encriptación de contraseñas)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de la autenticación de usuarios
    @Autowired
    public void autenticacio(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    // Configuración de las rutas de seguridad
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/adminpistas/**").permitAll()
                        .requestMatchers("/addPista/**").permitAll() // Asegura que los POST también sean permitidos
                        .requestMatchers("/usuario/**").permitAll()
                        .requestMatchers("/addJugador/**").permitAll()
                        .requestMatchers("/addJugadores/**").permitAll()
                        .requestMatchers("/menu_admin/**").permitAll()
                        .requestMatchers("/menu_principal/**").permitAll()
                        .anyRequest().authenticated())
                // Configuración del formulario de login
                .formLogin(form -> form
                .loginPage("/login") // Página personalizada de login
                .defaultSuccessUrl("/menu_principal", true) // Redirige a /menu_principal después del login exitoso
                .permitAll()
                )
                // Configuración del logout
                .logout(logout -> logout
                .deleteCookies("JSESSIONID") // Borra la cookie JSESSIONID al hacer logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Ruta de logout
                .permitAll()
                );

        return http.build();
    }
}
