package cetys.biblioteca.demos;

import cetys.biblioteca.auditoria.AuditoriaLogger;

/**
 * Demostracion ejecutable del patron Singleton (AuditoriaLogger).
 *
 * Demuestra:
 *   1. Que multiples llamadas a getInstancia() devuelven la MISMA instancia.
 *   2. Que el metodo registrar(evento, usuario) funciona.
 *   3. Que la identidad de objeto es identica (== devuelve true).
 *
 * Para ejecutar desde IntelliJ: click derecho -> Run 'DemoSingleton.main()'
 * Para ejecutar con Maven:
 *   mvn exec:java -Dexec.mainClass="cetys.biblioteca.demos.DemoSingleton"
 */
public class DemoSingleton {

    public static void main(String[] args) {
        System.out.println("=== Demo Singleton: AuditoriaLogger ===\n");

        AuditoriaLogger log1 = AuditoriaLogger.getInstancia();
        AuditoriaLogger log2 = AuditoriaLogger.getInstancia();

        log1.registrar("PRESTAMO_CREADO", "betsy@cetys.mx");
        log2.registrar("PRESTAMO_DEVUELTO", "admin@cetys.mx");

        System.out.println("\n--- Verificacion de instancia unica ---");
        System.out.println("¿log1 == log2? " + (log1 == log2));
        System.out.println("Hash de log1: " + System.identityHashCode(log1));
        System.out.println("Hash de log2: " + System.identityHashCode(log2));

        if (log1 == log2) {
            System.out.println("\n[OK] El Singleton funciona: ambas referencias apuntan al mismo objeto.");
        } else {
            System.out.println("\n[ERROR] El Singleton fallo: las referencias son distintas.");
        }
    }
}
