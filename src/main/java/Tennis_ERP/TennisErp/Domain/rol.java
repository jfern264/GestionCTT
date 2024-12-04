package Tennis_ERP.TennisErp.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class rol {  // Si decides seguir usando "rol" en minúsculas, está bien, pero asegúrate de mantener la coherencia

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String rol;
}
