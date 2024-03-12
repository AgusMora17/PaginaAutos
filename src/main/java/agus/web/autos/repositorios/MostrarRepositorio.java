/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package agus.web.autos.repositorios;

import agus.web.autos.entidades.Auto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author agust
 */
@Repository
public interface MostrarRepositorio extends JpaRepository<Auto, String>{
    
    @Query("SELECT c FROM Auto c WHERE c.baja IS NULL")
    public List<Auto> mostrarAutos();
    
}
