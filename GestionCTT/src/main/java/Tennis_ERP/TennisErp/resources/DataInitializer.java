package Tennis_ERP.TennisErp.resources;

// IMPORTS NECESARIOS
import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalTime;

import Tennis_ERP.TennisErp.dao.CategoriaDAO;
import Tennis_ERP.TennisErp.dao.EventDAO;
import Tennis_ERP.TennisErp.dao.LigaDAO;
import Tennis_ERP.TennisErp.dao.PistaDAO;
import Tennis_ERP.TennisErp.dao.RolDAO;
import Tennis_ERP.TennisErp.dao.UsuarioDAO;
import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Event;
import Tennis_ERP.TennisErp.domain.Liga;
import Tennis_ERP.TennisErp.domain.Pista;
import Tennis_ERP.TennisErp.domain.Rol;
import Tennis_ERP.TennisErp.domain.Usuario;
import Tennis_ERP.TennisErp.resources.Genero;
import Tennis_ERP.TennisErp.resources.GeneroCategoria;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioDAO uDao, RolDAO rDao, LigaDAO lDao,
            CategoriaDAO cDao, PistaDAO pDao, EventDAO eDao,
            PasswordEncoder encoder) {
        return args -> {
            // 1. ROLES (Admin y Jugador)
            Rol adminRol = rDao.findByNombreRol("ROLE_ADMIN");
            if (adminRol == null) {
                adminRol = new Rol();
                adminRol.setNombreRol("ROLE_ADMIN");
                adminRol = rDao.save(adminRol);
            }

            Rol jugadorRol = rDao.findByNombreRol("ROLE_JUGADOR");
            if (jugadorRol == null) {
                jugadorRol = new Rol();
                jugadorRol.setNombreRol("ROLE_JUGADOR");
                jugadorRol = rDao.save(jugadorRol);
            }

            // 2. LIGAS (3 ejemplos)
            if (lDao.count() == 0) {
                Liga liga1 = new Liga();
                liga1.setNombre("Liga Social Primavera");
                Liga liga2 = new Liga();
                liga2.setNombre("Torneo Nocturno Invierno");
                Liga liga3 = new Liga();
                liga3.setNombre("Circuito Senior");
                lDao.saveAll(List.of(liga1, liga2, liga3));

                // 3. CATEGORÍAS (Asociadas a la Liga 1)
                Categoria cat1 = new Categoria();
                cat1.setNombre("Primera Masculina");
                cat1.setDescripcion("Nivel avanzado");
                cat1.setLiga(liga1);
                cat1.setGenero(GeneroCategoria.MASCULINO); // Asegúrate de que este Enum exista

                Categoria cat2 = new Categoria();
                cat2.setNombre("Segunda Femenina");
                cat2.setDescripcion("Nivel intermedio");
                cat2.setLiga(liga1);
                cat2.setGenero(GeneroCategoria.FEMENINO);

                Categoria cat3 = new Categoria();
                cat3.setNombre("Mixto Oro");
                cat3.setDescripcion("Nivel competición");
                cat3.setLiga(liga1);
                cat3.setGenero(GeneroCategoria.MIXTO);

                cDao.saveAll(List.of(cat1, cat2, cat3));
            }

            // 4. PISTAS
            if (pDao.count() == 0) {
                Pista p1 = new Pista();
                p1.setNombrePista("Pista Central (Tierra)");
                Pista p2 = new Pista();
                p2.setNombrePista("Pista 2 (Rápida)");
                Pista p3 = new Pista();
                p3.setNombrePista("Pista 3 (Hierba)");
                pDao.saveAll(List.of(p1, p2, p3));

                // 5. EVENTOS (Asociados a Pista 1)
                Event ev1 = new Event(LocalDate.now(), LocalTime.of(10, 0), "Clase Particular", "Entrenamiento Saque",
                        p1);
                Event ev2 = new Event(LocalDate.now().plusDays(1), LocalTime.of(18, 0), "Partido Liga",
                        "Final de grupo", p1);
                Event ev3 = new Event(LocalDate.now().plusDays(2), LocalTime.of(12, 0), "Mantenimiento",
                        "Riego y cepillado", p1);
                eDao.saveAll(List.of(ev1, ev2, ev3));
            }

            // 6. USUARIOS (Admin y Jugador1)
            if (uDao.findByNombreUsuario("admin") == null) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("admin");
                admin.setPassword(encoder.encode("1"));
                admin.setEmail("admin@tennis.com");
                admin.setDni("12345678A");
                admin.setNombre("Admin");
                admin.setGenero(Genero.MASCULINO);
                admin.setRoles(Set.of(adminRol));
                uDao.save(admin);
            }

            if (uDao.findByNombreUsuario("jugador1") == null) {
                Usuario jugador = new Usuario();
                jugador.setNombreUsuario("jugador1");
                jugador.setPassword(encoder.encode("1"));
                jugador.setEmail("jugador@tennis.com");
                jugador.setDni("87654321B");
                jugador.setNombre("Juan Jugador");
                jugador.setGenero(Genero.MASCULINO);
                jugador.setRoles(Set.of(jugadorRol));
                uDao.save(jugador);
            }

            System.out.println("✅ Base de Datos inicializada con éxito.");
        };
    }
}