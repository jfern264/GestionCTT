package Tennis_ERP.TennisErp.domain;

import java.time.LocalDate;

public class Event {

    private LocalDate date;
    private String title;  // Nuevo campo para el título
    private String description;

    // Constructor modificado para incluir título
    public Event(String fecha, String titulo, String descripcion) {
        this.date = LocalDate.parse(fecha); // Asegúrate de manejar correctamente el formato de la fecha
        this.title = titulo;  // Asignar el título
        this.description = descripcion;
    }

    // Getters y setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
