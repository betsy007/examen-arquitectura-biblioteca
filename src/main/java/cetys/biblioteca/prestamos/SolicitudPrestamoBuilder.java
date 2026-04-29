package cetys.biblioteca.prestamos;

import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.usuarios.Estudiante;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder con metodos encadenables (fluent interface) para construir
 * SolicitudPrestamo.
 *
 * El metodo construir() valida invariantes (campos obligatorios y reglas
 * de negocio) ANTES de instanciar la solicitud.
 *
 * Acumula TODOS los errores antes de lanzar excepcion (mejor experiencia
 * que fallar al primer error).
 */
public class SolicitudPrestamoBuilder {

    // Visibles a nivel de paquete para que SolicitudPrestamo las lea.
    Estudiante estudiante;
    Libro libro;
    LocalDate fechaDevolucion;
    String notasEspeciales = null;            // default
    boolean renovacionAutomatica = false;     // default
    int numRenovaciones = 1;                  // default

    public SolicitudPrestamoBuilder conEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        return this;
    }

    public SolicitudPrestamoBuilder conLibro(Libro libro) {
        this.libro = libro;
        return this;
    }

    public SolicitudPrestamoBuilder conFechaDevolucion(LocalDate fecha) {
        this.fechaDevolucion = fecha;
        return this;
    }

    public SolicitudPrestamoBuilder conNotasEspeciales(String notas) {
        this.notasEspeciales = notas;
        return this;
    }

    public SolicitudPrestamoBuilder conRenovacionAutomatica(boolean activa) {
        this.renovacionAutomatica = activa;
        return this;
    }

    public SolicitudPrestamoBuilder conNumRenovaciones(int num) {
        this.numRenovaciones = num;
        return this;
    }

    /**
     * Valida invariantes y construye la solicitud inmutable.
     *
     * Validaciones:
     *   - estudiante, libro, fechaDevolucion son obligatorios.
     *   - fechaDevolucion no puede estar en el pasado.
     *   - numRenovaciones no puede ser negativo.
     *
     * @throws IllegalStateException con todos los errores encontrados,
     *         si las validaciones no pasan.
     */
    public SolicitudPrestamo construir() {
        List<String> errores = new ArrayList<>();

        if (estudiante == null) {
            errores.add("estudiante es obligatorio");
        }
        if (libro == null) {
            errores.add("libro es obligatorio");
        }
        if (fechaDevolucion == null) {
            errores.add("fechaDevolucion es obligatoria");
        } else if (fechaDevolucion.isBefore(LocalDate.now())) {
            errores.add("fechaDevolucion no puede estar en el pasado");
        }
        if (numRenovaciones < 0) {
            errores.add("numRenovaciones no puede ser negativo");
        }

        if (!errores.isEmpty()) {
            throw new IllegalStateException(
                "No se puede construir SolicitudPrestamo: " + String.join("; ", errores)
            );
        }

        return new SolicitudPrestamo(this);
    }
}
