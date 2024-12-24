package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id; // Campo público para acceso directo

    public String nombre; // Campo público para acceso directo

    public String contraseña; // Campo público para acceso directo

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    public rol rol; // Campo público para acceso directo
}
