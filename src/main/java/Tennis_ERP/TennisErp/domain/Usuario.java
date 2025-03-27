package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false)
    private String password;

    private String nombre;
    private String primerApellido;
    private String segundoApellido;

    @Column(nullable = false, unique = true)
    private String dni;

    private String dniFamiliar;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String telefono;
    private LocalDate fechaNacimiento;
    private String matricula;
    private String formaDePago;
    private String avatar;

    @ManyToMany
    @JoinTable(
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioCategoria> usuarioCategorias;
}

