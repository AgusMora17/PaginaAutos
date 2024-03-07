
package agus.web.autos.servicios;

import agus.web.autos.entidades.Auto;
import agus.web.autos.entidades.Foto;
import agus.web.autos.entidades.Usuario;
import agus.web.autos.enumeraciones.Marca;
import agus.web.autos.enumeraciones.Tipo;
import agus.web.autos.errores.ErrorServicio;
import agus.web.autos.repositorios.AutoRepositorio;
import agus.web.autos.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author agust
 */
@Service
public class AutoServicio {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private AutoRepositorio autoRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional
    public void agregarAuto(MultipartFile archivo,String idUsuario, String nombre, Marca marca, Tipo tipo) throws ErrorServicio{
        Usuario usuario=usuarioRepositorio.findById(idUsuario).get();
        
        validar(nombre,marca);
        
        Auto auto=new Auto();
        auto.setUsuario(usuario); //me faltó tirarle este ya que pasaba un perro sin id de usuario
        auto.setNombre(nombre);
        auto.setMarca(marca);
        auto.setAlta(new Date());
        auto.setTipo(tipo);
        
        Foto foto=fotoServicio.guardar(archivo);
        auto.setFoto(foto);
        
        autoRepositorio.save(auto);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo,String idUsuario, String idAuto, String nombre, Marca marca, Tipo tipo) throws ErrorServicio{
        validar(nombre,marca);
        
        Optional<Auto> respuesta = autoRepositorio.findById(idAuto);
        if (respuesta.isPresent()) {
            Auto auto=respuesta.get();
            if (auto.getUsuario().getId().equals(idUsuario)){
                auto.setNombre(nombre);
                auto.setMarca(marca);
                
                String idFoto =null;
                if(auto.getFoto() != null){
                    idFoto=auto.getFoto().getId();
                }
                Foto foto =fotoServicio.actualizar(idFoto, archivo);
                auto.setFoto(foto);
                auto.setTipo(tipo);
                
                autoRepositorio.save(auto);
            } else {
                throw new ErrorServicio("No tiene permisos suficientes para realizar la operación");
            }
        } else {
            throw new ErrorServicio("No existe un auto con el identificador solicitado");
        }
    }
    
    @Transactional
    public void eliminar(String idUsuario,String idAuto) throws ErrorServicio{
        Optional<Auto> respuesta = autoRepositorio.findById(idAuto);
        if (respuesta.isPresent()) {
            Auto auto=respuesta.get();
            if (auto.getUsuario().getId().equals(idUsuario)){
                auto.setBaja(new Date());
                autoRepositorio.save(auto);
            }
        } else {
            throw new ErrorServicio("No existe un auto con el identificador solicitado");
        }
    }
    
    
    public void validar (String nombre, Marca marca) throws ErrorServicio{
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del auto no puede ser nulo o vacío");
        }
        if (marca == null) {
            throw new ErrorServicio("La marca del auto no puede ser nulo");
        }
    }
    
    public Auto buscarPorId(String id) throws ErrorServicio {
        Optional<Auto> respuesta= autoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El auto solicitado no existe");
        }
            
    }
    
    public List<Auto> buscarAutoPorUsuario (String id) {
        return autoRepositorio.buscarAutoPorUsuario(id);
    }
    
}
