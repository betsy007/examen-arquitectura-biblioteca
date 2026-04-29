package cetys.biblioteca.dominio.puertos;

/**
 * Puerto de salida para enviar notificaciones a los usuarios.
 *
 * Cualquier mecanismo concreto (SMTP, SendGrid, SMS, push, in-app) es un
 * adaptador en la capa de infraestructura que implementa esta interfaz.
 */
public interface ServicioNotificacion {

    /**
     * Envia una notificacion al destinatario indicado.
     *
     * @param destinatario identificador del usuario (email, userId, telefono, etc.)
     * @param asunto       asunto o titulo de la notificacion
     * @param cuerpo       contenido del mensaje
     */
    void notificar(String destinatario, String asunto, String cuerpo);
}
