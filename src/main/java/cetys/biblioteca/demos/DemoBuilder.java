package cetys.biblioteca.demos;

import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.prestamos.SolicitudPrestamo;
import cetys.biblioteca.prestamos.SolicitudPrestamoBuilder;
import cetys.biblioteca.usuarios.Estudiante;
import java.time.LocalDate;

/**
 * Demostracion ejecutable del patron Builder (SolicitudPrestamoBuilder).
 *
 * Demuestra:
 *   1. Tres ejemplos de uso con distintas combinaciones de atributos opcionales.
 *   2. Que la validacion en construir() atrapa errores antes de instanciar.
 *   3. Que la solicitud resultante es inmutable (sin setters).
 */
public class DemoBuilder {

    public static void main(String[] args) {
        System.out.println("=== Demo Builder: SolicitudPrestamoBuilder ===\n");

        Estudiante betsy = new Estudiante("CT-2024-001", "Betsy Danaeth Arceo Rivera");

        Libro cleanArch = new Libro("978-0134494166", "Clean Architecture",
                                    "Robert C. Martin", 2017, true);
        Libro pragmatic = new Libro("978-0135957059", "The Pragmatic Programmer",
                                    "Hunt & Thomas", 2019, true);
        Libro dddBook   = new Libro("978-0321125217", "Domain-Driven Design",
                                    "Eric Evans", 2003, true);

        // ---- Ejemplo 1: solo campos obligatorios (toma todos los defaults) ----
        SolicitudPrestamo s1 = new SolicitudPrestamoBuilder()
            .conEstudiante(betsy)
            .conLibro(cleanArch)
            .conFechaDevolucion(LocalDate.now().plusDays(14))
            .construir();

        // ---- Ejemplo 2: con notas especiales y renovacion automatica ----
        SolicitudPrestamo s2 = new SolicitudPrestamoBuilder()
            .conEstudiante(betsy)
            .conLibro(pragmatic)
            .conFechaDevolucion(LocalDate.now().plusDays(21))
            .conNotasEspeciales("Material para tesis - prioridad alta")
            .conRenovacionAutomatica(true)
            .construir();

        // ---- Ejemplo 3: con numero de renovaciones personalizado ----
        SolicitudPrestamo s3 = new SolicitudPrestamoBuilder()
            .conEstudiante(betsy)
            .conLibro(dddBook)
            .conFechaDevolucion(LocalDate.now().plusDays(30))
            .conNumRenovaciones(3)
            .construir();

        System.out.println("Ejemplo 1 (solo obligatorios):");
        System.out.println("  " + s1);
        System.out.println("\nEjemplo 2 (con notas y renovacion automatica):");
        System.out.println("  " + s2);
        System.out.println("\nEjemplo 3 (con num renovaciones):");
        System.out.println("  " + s3);

        // ---- Ejemplo 4: demuestra la validacion ----
        System.out.println("\n--- Validacion: solicitud incompleta ---");
        try {
            new SolicitudPrestamoBuilder()
                .conEstudiante(betsy)
                // falta libro y fechaDevolucion
                .construir();
        } catch (IllegalStateException e) {
            System.out.println("[OK] Validacion atrapo el error:");
            System.out.println("  " + e.getMessage());
        }
    }
}
