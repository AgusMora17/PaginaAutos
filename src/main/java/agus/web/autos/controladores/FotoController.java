
package agus.web.autos.controladores;

import agus.web.autos.entidades.Auto;
import agus.web.autos.entidades.Usuario;
import agus.web.autos.errores.ErrorServicio;
import agus.web.autos.servicios.AutoServicio;
import agus.web.autos.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @author agust
 */

@Controller
@RequestMapping("/foto")
public class FotoController {
    
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private AutoServicio autoServicio;
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) throws ErrorServicio {
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            if (usuario.getFoto() == null) {
                throw new ErrorServicio("El usuario no tiene una foto asignada.");
            }
            
            
            byte[] foto=usuario.getFoto().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
    @GetMapping("/auto/{id}")
    public ResponseEntity<byte[]> fotoAuto(@PathVariable String id) throws ErrorServicio {
        try {
            Auto auto = autoServicio.buscarPorId(id);
            if (auto.getFoto() == null) {
                throw new ErrorServicio("El auto no tiene una foto asignada.");
            }
            
            
            byte[] foto=auto.getFoto().getContenido();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }
    
   
    
}
