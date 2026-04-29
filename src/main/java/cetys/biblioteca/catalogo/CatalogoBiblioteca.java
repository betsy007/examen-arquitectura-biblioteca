package cetys.biblioteca.catalogo;

/**
 * Interfaz Target que el sistema interno espera para consultar libros.
 * Use cases, controllers, tests dependen SOLO de esta abstraccion.
 *
 * El proveedor real (Catalogo CETYS via SOAP) no implementa esta interfaz
 * directamente; un Adapter la implementa y delega al proveedor traduciendo
 * los parametros y resultados.
 */
public interface CatalogoBiblioteca {

    /**
     * Busca un libro por su ISBN.
     *
     * @param isbn codigo ISBN del libro
     * @return el Libro encontrado (con datos traducidos del formato externo)
     */
    Libro buscarLibro(String isbn);
}
