
package agus.web.autos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author agust
 */
@Service
public class NotificacionServicio {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Async //asincrono no espera a que se termine de mandar el mail, lo ejectuta en un hilo paralelo
    public void enviar(String cuerpo, String titulo, String mail){
        SimpleMailMessage mensaje=new SimpleMailMessage();
        mensaje.setTo(mail);
        mensaje.setFrom("agustin40918@gmail.com");
        mensaje.setSubject(titulo);
        mensaje.setText(cuerpo);
        
        mailSender.send(mensaje);
        
        
    }
    
}
