package cetys.biblioteca.auditoria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton que centraliza el registro de auditoria del sistema.
 * Garantiza una unica instancia en toda la JVM (un unico log centralizado,
 * tal como exige el rector en el caso de estudio).
 *
 * Implementacion: double-checked locking con campo volatile para thread-safety.
 *
 * @see cetys.biblioteca.demos.DemoSingleton para una demostracion ejecutable.
 */
public final class AuditoriaLogger {

    /**
     * Referencia unica a la instancia. volatile garantiza visibilidad
     * entre hilos (cualquier thread vera el ultimo valor escrito).
     */
    private static volatile AuditoriaLogger instancia;

    /**
     * Constructor PRIVADO: nadie fuera de la clase puede invocar 'new'.
     * Adicionalmente valida que no se rompa el patron via reflexion.
     */
    private AuditoriaLogger() {
        if (instancia != null) {
            throw new IllegalStateException(
                "AuditoriaLogger ya fue inicializado. Use getInstancia()."
            );
        }
    }

    /**
     * Punto de acceso global con double-checked locking.
     * Thread-safe y perezoso (la instancia se crea solo cuando se necesita).
     */
    public static AuditoriaLogger getInstancia() {
        if (instancia == null) {
            synchronized (AuditoriaLogger.class) {
                if (instancia == null) {
                    instancia = new AuditoriaLogger();
                }
            }
        }
        return instancia;
    }

    /**
     * Registra un evento de auditoria con marca temporal y usuario.
     * Sincronizado para evitar entrelazado de escrituras en concurrencia.
     *
     * En produccion este metodo escribiria a la BD de Auditoria (append-only).
     *
     * @param evento  identificador del evento (ej. "PRESTAMO_REGISTRADO")
     * @param usuario identificador del usuario que ejecuto la accion
     */
    public synchronized void registrar(String evento, String usuario) {
        String marca = LocalDateTime.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.printf("  [AUDITORIA %s] usuario=%s evento=%s%n", marca, usuario, evento);
    }
}
