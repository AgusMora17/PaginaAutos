
package agus.web.autos.controladores;

import agus.web.autos.entidades.Auto;
import agus.web.autos.enumeraciones.Marca;
import agus.web.autos.enumeraciones.Tipo;
import agus.web.autos.errores.ErrorServicio;
import agus.web.autos.repositorios.MostrarRepositorio;
import agus.web.autos.servicios.AutoServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author agust
 */

@Controller
@RequestMapping("/vehiculos")
public class MostrarController {
    
    @Autowired
    MostrarRepositorio mostrarRepositorio;
    
    @Autowired     
    private AutoServicio autoServicio;
    
    @GetMapping("/mostrar-autos")
    public String misAutos(ModelMap model) {
        
        
        List<Auto> autos= mostrarRepositorio.mostrarAutos();
        model.put("autos", autos);
        
        return "mostrar"; 
    }
    
    @GetMapping("/mostrar-vehiculo")
    public String elVehiculo(@RequestParam(required=false) String id, ModelMap model) {
        Auto auto = new Auto();
        if (id != null && !id.isEmpty()) {
            try {
                auto = autoServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(AutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        model.put("perfil", auto);
        model.put("marcas", Marca.values());
        model.put("tipos", Tipo.values());
        return "automostrar.html";
    }
    
    
    
    
    
}
