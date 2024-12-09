package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String contraseña;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false) // Relación con la tabla roles
    private rol rol;

    // Métodos getter
    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public rol getRol() {
        return rol;
    }

    // Métodos setter
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setRol(rol rol) {
        this.rol = rol;
    }
}
