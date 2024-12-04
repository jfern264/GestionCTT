package Tennis_ERP.TennisErp.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class usuario {  // Manteniendo la convención en minúsculas

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String contraseña;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private rol rol;  // Mantén "rol" en minúsculas, aunque recomiendo que uses "Rol"
}
