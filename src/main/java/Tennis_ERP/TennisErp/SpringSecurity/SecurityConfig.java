package Tennis_ERP.TennisErp.SpringSecurity;

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
    
        // A partir de Spring Security 5.7+, ya no se recomienda sobrescribir directamente AuthenticationManagerBuilder.
        // En su lugar, se utiliza AuthenticationConfiguration, y Spring se encarga automáticamente de configurar
        // el AuthenticationManager si detecta un UserDetailsService y un PasswordEncoder en el contexto.
        //
        // ¿Por qué funciona sin configurarlo manualmente?
        // Porque Spring Boot detecta que existe un @Bean de PasswordEncoder y un @Service("userDetailsService")
        // que implementa UserDetailsService, y los registra automáticamente para el AuthenticationManager.


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
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/Usuario/**").hasAnyAuthority("Admin", "Jugador")
                        .requestMatchers("/menu_admin/**").hasAnyAuthority("Admin")
                        .requestMatchers("/menuAdmin/**").hasAnyAuthority("Admin")
                        .requestMatchers("/admincalendario/**").hasAnyAuthority("Admin")
                        .requestMatchers("/adminpista/**").hasAnyAuthority("Admin")
                        .requestMatchers("/adminjugadores/**").hasAuthority("Admin")
                        .requestMatchers("/menu_principal/**").hasAnyAuthority("Admin", "Jugador")
                        .requestMatchers("/calendario/**").hasAnyAuthority("Admin", "Jugador")
                        .requestMatchers("/jugadores/**").hasAnyAuthority("Admin", "Jugador")
                        .requestMatchers("/trabajadores/**").hasAnyAuthority("Admin", "Jugador")
                        .requestMatchers("/pistas/**").hasAnyAuthority("Admin", "Jugador")
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers(("/images/**")).permitAll()
                        .anyRequest().authenticated()
        )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/menu_principal", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/errors/error403")
                );

        return http.build();
    }
}
