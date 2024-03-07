
package agus.web.autos.controladores;

import agus.web.autos.entidades.Auto;
import agus.web.autos.entidades.Usuario;
import agus.web.autos.enumeraciones.Marca;
import agus.web.autos.enumeraciones.Tipo;
import agus.web.autos.errores.ErrorServicio;
import agus.web.autos.servicios.AutoServicio;
import agus.web.autos.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author agust
 */

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("/auto")
public class AutoController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
                   
    @Autowired     
    private AutoServicio autoServicio;
    
    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            autoServicio.eliminar(login.getId(), id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/auto/mis-autos";
    }
    
    
    @GetMapping("/mis-autos")
    public String misAutos(HttpSession session, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }
        
        List<Auto> autos= autoServicio.buscarAutoPorUsuario(login.getId());
        model.put("autos", autos);
        
        return "autos"; 
    }
    
    
    
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session ,@RequestParam(required=false) String id, @RequestParam(required=false) String accion, ModelMap model) {
        
        if (accion == null) {
            accion = "Crear";
        }
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }
        
        Auto auto = new Auto();
        if (id != null && !id.isEmpty()) {
            try {
                auto = autoServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(AutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        model.put("perfil", auto);
        model.put("accion", accion);
        model.put("marcas", Marca.values());
        model.put("tipos", Tipo.values());
        return "auto.html";
        
    }
    
    
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap modelo,HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Marca marca, @RequestParam Tipo tipo) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        
        if (login == null) {
            return "redirect:/login";
        }
        try {
            
            if (id == null || id.isEmpty()) {
                autoServicio.agregarAuto(archivo, login.getId(), nombre, marca, tipo);
            } else {
                autoServicio.modificar(archivo, login.getId(), id, nombre, marca, tipo);
            }
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            Auto auto = new Auto();
            auto.setId(id);
            auto.setNombre(nombre);
            auto.setMarca(marca);
            auto.setTipo(tipo);
            
            modelo.put("accion", "Actualizar");
            modelo.put("marcas", Marca.values());
            modelo.put("tipos", Tipo.values());
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", auto);
            return "auto.html";
        }
        
        
        
    }
    
    
    
}
