package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Actividades")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // La clave primaria de la entidad
    
    private LocalDate date;
    private LocalTime time;  // Nuevo campo para la hora
    private String title;
    private String description;

    // Constructor modificado para incluir hora
    public Event(String fecha, String hora, String titulo, String descripcion) {
        this.date = LocalDate.parse(fecha); // Asegúrate de manejar correctamente el formato de la fecha
        this.time = LocalTime.parse(hora);  // Asignar la hora
        this.title = titulo;
        this.description = descripcion;
    }

    // Getters y setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
