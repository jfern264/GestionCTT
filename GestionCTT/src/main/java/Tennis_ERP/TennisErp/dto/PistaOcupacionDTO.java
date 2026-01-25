package Tennis_ERP.TennisErp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data // Genera Getters, Setters, toString, etc.
@AllArgsConstructor // Genera el constructor PistaOcupacionDTO(String, List<Integer>)
@NoArgsConstructor  // Constructor vacío necesario para frameworks de serialización
public class PistaOcupacionDTO {
    private String nombrePista;
    private List<Integer> horasOcupadas;
}