package cetys.biblioteca.demos;

import cetys.biblioteca.catalogo.CatalogoBiblioteca;
import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.catalogo.adaptadores.CatalogoCETYS;
import cetys.biblioteca.catalogo.adaptadores.CatalogoCETYSAdapter;

/**
 * Demostracion ejecutable del patron Adapter (CatalogoCETYSAdapter).
 *
 * Demuestra:
 *   1. Que el cliente depende de la interfaz CatalogoBiblioteca, no del
 *      proveedor externo CatalogoCETYS.
 *   2. Que el adapter traduce el llamado interno (buscarLibro(isbn)) a la
 *      firma externa (consultarObra(codigo, formato)).
 *   3. Que la respuesta SOAP se traduce al modelo de dominio Libro.
 */
public class DemoAdapter {

    public static void main(String[] args) {
        System.out.println("=== Demo Adapter: CatalogoCETYSAdapter ===\n");

        // En produccion (Spring) esto seria un @Bean de configuracion.
        // El cliente solo recibe CatalogoBiblioteca; no sabe que hay un adapter.
        CatalogoBiblioteca catalogo = new CatalogoCETYSAdapter(new CatalogoCETYS());

        Libro libro = catalogo.buscarLibro("978-0134494166");

        System.out.println("Libro encontrado:");
        System.out.printf("  ISBN  : %s%n", libro.getIsbn());
        System.out.printf("  Titulo: %s%n", libro.getTitulo());
        System.out.printf("  Autor : %s%n", libro.getAutor());
        System.out.printf("  Anio  : %d%n", libro.getAnioPublicacion());
        System.out.printf("  Estado: %s%n",
            libro.isDisponible() ? "DISPONIBLE" : "NO DISPONIBLE");

        System.out.println("\n[OK] El cliente nunca toco SOAP ni vio ResultadoSOAP.");
        System.out.println("     El Adapter tradujo todo de forma transparente.");
    }
}
