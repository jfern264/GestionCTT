package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Asegúrate de que se genere el ID automáticamente
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType rol;

    public enum RoleType {
        Admin, Trabajador, Usuario
    }
}
