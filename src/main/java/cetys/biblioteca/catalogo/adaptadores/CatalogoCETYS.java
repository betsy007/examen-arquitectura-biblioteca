package cetys.biblioteca.catalogo.adaptadores;

/**
 * Servicio del Catalogo CETYS (Adaptee). Expone la firma original:
 *   consultarObra(codigoCETYS: String, formato: String): ResultadoSOAP
 *
 * Esta firma es DIFERENTE a la que el sistema interno espera:
 *   buscarLibro(isbn: String): Libro
 *
 * IMPORTANTE: Esta clase NO se modifica. Es codigo de un sistema externo.
 * El Adapter (CatalogoCETYSAdapter) es quien se encarga de traducir entre
 * ambos mundos.
 *
 * En produccion esta clase haria una llamada SOAP real (con javax.xml.ws,
 * Apache CXF, etc.). Aqui se simula la respuesta para fines del examen.
 */
public class CatalogoCETYS {

    public ResultadoSOAP consultarObra(String codigoCETYS, String formato) {
        // Simulacion: en produccion seria una llamada SOAP/HTTPS real
        return new ResultadoSOAP(
            codigoCETYS,
            "Clean Architecture",
            "Robert C. Martin",
            "2017-09-20",
            "DISPONIBLE"
        );
    }
}
