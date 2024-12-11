package Tennis_ERP.TennisErp.domain;

import java.time.LocalDate;

public class Event {
    private LocalDate date;
    private String description;

    public Event(String fecha, String descripcion) {
        this.date = LocalDate.parse(fecha); // Asegúrate de manejar correctamente el formato de la fecha
        this.description = descripcion;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
