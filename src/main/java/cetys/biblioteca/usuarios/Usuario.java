package cetys.biblioteca.usuarios;

/**
 * Contrato comun para todos los tipos de usuario del sistema.
 * El codigo cliente (controllers, use cases) depende SOLO de esta interfaz,
 * nunca de las clases concretas (Estudiante, Bibliotecario, Admin, Posgrado).
 *
 * Esta es la base que permite:
 *   - Cumplir el Liskov Substitution Principle (LSP).
 *   - Aplicar Dependency Inversion (DIP).
 *   - Extender el sistema con nuevos tipos sin modificar codigo existente (OCP).
 */
public interface Usuario {
    String getId();
    String getNombre();
    String getRol();
    int getLimitePrestamos();
    boolean puedeAprobarPrestamos();
}
