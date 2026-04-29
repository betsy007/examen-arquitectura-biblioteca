package cetys.biblioteca.prestamos;

import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.usuarios.Estudiante;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa una solicitud de prestamo.
 *
 * Esta clase es INMUTABLE: una vez construida, sus atributos no cambian.
 * Solo se puede crear a traves de SolicitudPrestamoBuilder.
 *
 * Decisiones de diseno:
 *   - 'final' en la clase: no se puede subclasear (subclases podrian romper inmutabilidad).
 *   - 'final' en cada atributo: el compilador garantiza que solo se asignan en el constructor.
 *   - Solo getters, NINGUN setter.
 *   - Constructor package-private: solo el Builder (mismo paquete) puede invocarlo.
 *
 * @see SolicitudPrestamoBuilder
 */
public final class SolicitudPrestamo {

    private final Estudiante estudiante;
    private final Libro libro;
    private final LocalDate fechaDevolucion;
    private final String notasEspeciales;
    private final boolean renovacionAutomatica;
    private final int numRenovaciones;

    /**
     * Constructor de paquete (package-private): solo el Builder, que vive
     * en el mismo paquete, puede invocarlo. El cliente NO puede hacer 'new'.
     */
    SolicitudPrestamo(SolicitudPrestamoBuilder b) {
        this.estudiante           = b.estudiante;
        this.libro                = b.libro;
        this.fechaDevolucion      = b.fechaDevolucion;
        this.notasEspeciales      = b.notasEspeciales;
        this.renovacionAutomatica = b.renovacionAutomatica;
        this.numRenovaciones      = b.numRenovaciones;
    }

    public Estudiante getEstudiante()         { return estudiante; }
    public Libro      getLibro()              { return libro; }
    public LocalDate  getFechaDevolucion()    { return fechaDevolucion; }
    public String     getNotasEspeciales()    { return notasEspeciales; }
    public boolean    isRenovacionAutomatica(){ return renovacionAutomatica; }
    public int        getNumRenovaciones()    { return numRenovaciones; }

    @Override
    public String toString() {
        return "SolicitudPrestamo{" +
               "estudiante=" + estudiante.getNombre() +
               ", libro='" + libro.getTitulo() + '\'' +
               ", fechaDevolucion=" + fechaDevolucion +
               ", renovacionAutomatica=" + renovacionAutomatica +
               ", numRenovaciones=" + numRenovaciones +
               (notasEspeciales != null ? ", notas='" + notasEspeciales + '\'' : "") +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SolicitudPrestamo s)) return false;
        return renovacionAutomatica == s.renovacionAutomatica
            && numRenovaciones == s.numRenovaciones
            && Objects.equals(estudiante, s.estudiante)
            && Objects.equals(libro, s.libro)
            && Objects.equals(fechaDevolucion, s.fechaDevolucion)
            && Objects.equals(notasEspeciales, s.notasEspeciales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estudiante, libro, fechaDevolucion,
                            notasEspeciales, renovacionAutomatica, numRenovaciones);
    }
}
