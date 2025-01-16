package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "equipos")
@Data 
public class Equipo { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String liga;
    private int puntos;
    private String jugadores;
    private String categoria;
    private int partidos;
    private String objetivo;

    @Column(name = "precio_liga")
    private double precioLiga;
}
