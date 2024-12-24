package Tennis_ERP.TennisErp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Entity
@Table(name = "Actividades")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID es Long

    private LocalDate date;
    private LocalTime time;  // Nuevo campo para la hora
    private String title;
    private String description;

    // El constructor sin el ID, ya que el ID es autogenerado
    public Event(LocalDate date, LocalTime time, String title, String description) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }
}
