package cetys.biblioteca.dominio.modelo;

import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.usuarios.Usuario;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Modelo de dominio: representa un prestamo de libro.
 *
 * Vive en la capa de dominio (mas interna de Clean Architecture).
 * No depende de ningun framework, base de datos o tecnologia externa.
 */
public class Prestamo {

    private final String id;
    private final Usuario usuario;
    private final Libro libro;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaDevolucion;
    private boolean devuelto;

    public Prestamo(Usuario usuario, Libro libro, LocalDate fechaDevolucion) {
        this.id = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = false;
    }

    public String getId()                 { return id; }
    public Usuario getUsuario()           { return usuario; }
    public Libro getLibro()               { return libro; }
    public LocalDate getFechaPrestamo()   { return fechaPrestamo; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public boolean isDevuelto()           { return devuelto; }

    public void marcarDevuelto() {
        if (devuelto) {
            throw new IllegalStateException("Prestamo ya estaba devuelto");
        }
        this.devuelto = true;
    }
}
