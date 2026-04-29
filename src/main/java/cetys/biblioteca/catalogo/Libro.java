package cetys.biblioteca.catalogo;

/**
 * Modelo de libro del dominio interno.
 * El resto del sistema (use cases, controllers, builders) solo conoce
 * esta clase. La clase ResultadoSOAP del proveedor externo NUNCA llega
 * hasta aqui; el Adapter se encarga de traducir.
 */
public class Libro {

    private final String isbn;
    private final String titulo;
    private final String autor;
    private final int anioPublicacion;
    private final boolean disponible;

    public Libro(String isbn, String titulo, String autor,
                 int anioPublicacion, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.disponible = disponible;
    }

    public String getIsbn()         { return isbn; }
    public String getTitulo()       { return titulo; }
    public String getAutor()        { return autor; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public boolean isDisponible()   { return disponible; }
}
