package cetys.biblioteca.catalogo.adaptadores;

import cetys.biblioteca.catalogo.CatalogoBiblioteca;
import cetys.biblioteca.catalogo.Libro;
import java.time.LocalDate;

/**
 * Adapter (Object Adapter) que traduce entre la interfaz que el sistema
 * interno espera (CatalogoBiblioteca) y la API real del proveedor CETYS
 * (CatalogoCETYS).
 *
 * Patron usado: Object Adapter (composicion, no herencia).
 * El adaptador "tiene un" CatalogoCETYS y lo usa internamente.
 *
 * Ventajas frente a Class Adapter (herencia):
 *   - Permite cambiar la instancia del Adaptee en tiempo de ejecucion
 *     (util para tests con mocks).
 *   - Java no soporta herencia multiple de clases, asi que no podriamos
 *     heredar de CatalogoCETYS y al mismo tiempo de otra clase base.
 *
 * Si CETYS cambia de proveedor con interfaz totalmente diferente:
 *   - Solo se crea un nuevo adapter (ej. CatalogoNuevoProveedorAdapter).
 *   - Los use cases NO se modifican.
 */
public class CatalogoCETYSAdapter implements CatalogoBiblioteca {

    private final CatalogoCETYS catalogoExterno;

    public CatalogoCETYSAdapter(CatalogoCETYS catalogoExterno) {
        this.catalogoExterno = catalogoExterno;
    }

    @Override
    public Libro buscarLibro(String isbn) {
        // 1. Traducir el parametro: el sistema interno usa ISBN,
        //    CETYS usa codigoCETYS. Asumimos mapeo 1:1 o tabla de conversion.
        String codigoCETYS = mapearIsbnACodigoCETYS(isbn);

        // 2. Llamar al servicio externo con SU firma.
        ResultadoSOAP resultado = catalogoExterno.consultarObra(codigoCETYS, "JSON");

        // 3. Traducir la respuesta al modelo del dominio interno.
        return new Libro(
            isbn,
            resultado.getTituloObra(),
            resultado.getAutorPrincipal(),
            extraerAnio(resultado.getFechaPublicacion()),
            "DISPONIBLE".equalsIgnoreCase(resultado.getEstatus())
        );
    }

    /**
     * Mapea el ISBN interno al codigo que entiende el catalogo CETYS.
     * En produccion consultaria una tabla de equivalencias en BD.
     */
    private String mapearIsbnACodigoCETYS(String isbn) {
        // Simplificacion: el codigo CETYS es el ISBN sin guiones.
        return isbn.replace("-", "");
    }

    /**
     * Extrae el anio de una fecha en formato ISO "YYYY-MM-DD".
     */
    private int extraerAnio(String fechaIso) {
        return LocalDate.parse(fechaIso).getYear();
    }
}
