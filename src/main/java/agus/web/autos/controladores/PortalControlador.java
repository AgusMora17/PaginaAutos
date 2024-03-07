/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agus.web.autos.controladores;

import agus.web.autos.entidades.Zona;
import agus.web.autos.errores.ErrorServicio;
import agus.web.autos.repositorios.ZonaRepositorio;
import agus.web.autos.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author agust
 */

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')") 
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model){
        if (error != null) {
            model.put("error", "Nombre de usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente de la plataforma");
        }
        return "login.html";
    }
    
    @GetMapping("/registro")
    public String registro(ModelMap modelo){
        List<Zona> zonas= zonaRepositorio.findAll();
        modelo.put("zonas", zonas);
        return "registro.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,MultipartFile archivo,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String mail,@RequestParam String clave1,@RequestParam String clave2, @RequestParam String idZona){
        
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, mail, clave1, clave2, idZona);
        } catch (ErrorServicio ex) {
            List<Zona> zonas= zonaRepositorio.findAll();
            modelo.put("zonas", zonas);
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "registro.html";
        }
        modelo.put("titulo", "Bienvenido a Pagina de Autos");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
        return "exito.html";
    }
    
}
