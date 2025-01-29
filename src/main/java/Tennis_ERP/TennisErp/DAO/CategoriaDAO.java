/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Tennis_ERP.TennisErp.DAO;

import Tennis_ERP.TennisErp.domain.Categoria;
import Tennis_ERP.TennisErp.domain.Jugadores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adria
 */
@Repository
public interface CategoriaDAO extends JpaRepository<Categoria, Long>{

    public Categoria save(Categoria categoria);

    public void deleteById(Long id);

}
