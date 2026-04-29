package cetys.biblioteca.catalogo.adaptadores;

/**
 * Clase del proveedor CETYS. Representa el objeto que ya viene
 * "parseado" desde el cliente SOAP (en produccion vendria generado
 * por wsimport o similar).
 *
 * IMPORTANTE: Esta clase NO se modifica. Es codigo de un sistema externo
 * sobre el que no tenemos control. La unica forma de adaptarla a nuestra
 * interfaz interna es a traves de un Adapter (CatalogoCETYSAdapter).
 */
public class ResultadoSOAP {

    private final String codigoObra;
    private final String tituloObra;
    private final String autorPrincipal;
    private final String fechaPublicacion;   // formato "YYYY-MM-DD"
    private final String estatus;            // "DISPONIBLE", "PRESTADO", "RESERVADO"

    public ResultadoSOAP(String codigoObra, String tituloObra,
                         String autorPrincipal, String fechaPublicacion,
                         String estatus) {
        this.codigoObra = codigoObra;
        this.tituloObra = tituloObra;
        this.autorPrincipal = autorPrincipal;
        this.fechaPublicacion = fechaPublicacion;
        this.estatus = estatus;
    }

    public String getCodigoObra()       { return codigoObra; }
    public String getTituloObra()       { return tituloObra; }
    public String getAutorPrincipal()   { return autorPrincipal; }
    public String getFechaPublicacion() { return fechaPublicacion; }
    public String getEstatus()          { return estatus; }
}
