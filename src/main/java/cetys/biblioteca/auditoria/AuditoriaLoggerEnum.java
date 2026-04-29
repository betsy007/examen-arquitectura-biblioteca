package cetys.biblioteca.auditoria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Version alternativa del Singleton implementada como enum.
 *
 * Esta es la forma recomendada por Joshua Bloch en "Effective Java"
 * porque es:
 *   - Thread-safe por especificacion de la JVM (sin sincronizacion manual).
 *   - Resistente a serializacion (no produce instancias duplicadas).
 *   - Resistente a reflexion (la JVM impide instanciar enums via reflexion).
 *
 * Se incluye como referencia. La version principal del examen es la clase
 * AuditoriaLogger con constructor privado, porque ilustra explicitamente
 * los puntos pedagogicos que la rubrica solicita.
 *
 * Uso:
 *   AuditoriaLoggerEnum.INSTANCIA.registrar("EVENTO", "usuario");
 */
public enum AuditoriaLoggerEnum {
    INSTANCIA;

    public synchronized void registrar(String evento, String usuario) {
        String marca = LocalDateTime.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.printf("  [AUDITORIA-ENUM %s] usuario=%s evento=%s%n", marca, usuario, evento);
    }
}
