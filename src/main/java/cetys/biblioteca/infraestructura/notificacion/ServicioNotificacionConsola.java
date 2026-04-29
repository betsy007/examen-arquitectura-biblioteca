package cetys.biblioteca.infraestructura.notificacion;

import cetys.biblioteca.dominio.puertos.ServicioNotificacion;

/**
 * Implementacion de ServicioNotificacion que escribe a la consola.
 * Util para demos y tests. En produccion se reemplazaria por
 * ServicioEmailSMTP, ServicioSendGrid, ServicioSMSTwilio, etc.
 *
 * Demuestra que cambiar el canal de notificacion NO requiere modificar
 * el use case: solo se inyecta otro adapter que implemente la misma interfaz.
 */
public class ServicioNotificacionConsola implements ServicioNotificacion {

    @Override
    public void notificar(String destinatario, String asunto, String cuerpo) {
        System.out.printf("  [NOTIFICACION -> %s] %s :: %s%n",
            destinatario, asunto, cuerpo);
    }
}
