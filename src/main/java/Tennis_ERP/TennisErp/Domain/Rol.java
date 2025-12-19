package Tennis_ERP.TennisErp.Domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreRol;
}
