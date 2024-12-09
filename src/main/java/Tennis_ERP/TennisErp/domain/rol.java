package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String rol;

    // Método getter para el campo 'rol'
    public String getRol() {
        return rol;
    }

    // También puedes agregar un setter si lo necesitas
    public void setRol(String rol) {
        this.rol = rol;
    }
}
