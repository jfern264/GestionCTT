/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Trabajadores")
@Data
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // El ID se genera automáticamente (autoincrement)
    private Long id;

    private String nombre;
    private String apellidos;
    private String dni;       // Nuevo campo para el DNI
    private String correo;    // Nuevo campo para el correo electrónico

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)  // Relación con la tabla "rol" y columna "rol_id"
    private rol rol;
}