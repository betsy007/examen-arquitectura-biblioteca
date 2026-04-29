package cetys.biblioteca.demos;

import cetys.biblioteca.aplicacion.RegistrarPrestamoUseCase;
import cetys.biblioteca.catalogo.CatalogoBiblioteca;
import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.catalogo.adaptadores.CatalogoCETYS;
import cetys.biblioteca.catalogo.adaptadores.CatalogoCETYSAdapter;
import cetys.biblioteca.dominio.modelo.Prestamo;
import cetys.biblioteca.dominio.puertos.PagosGateway;
import cetys.biblioteca.dominio.puertos.RepositorioPrestamos;
import cetys.biblioteca.dominio.puertos.ServicioNotificacion;
import cetys.biblioteca.infraestructura.notificacion.ServicioNotificacionConsola;
import cetys.biblioteca.infraestructura.pagos.PagosGatewaySimulado;
import cetys.biblioteca.infraestructura.persistencia.RepoPrestamosEnMemoria;
import cetys.biblioteca.prestamos.SolicitudPrestamo;
import cetys.biblioteca.prestamos.SolicitudPrestamoBuilder;
import cetys.biblioteca.usuarios.FabricaDeUsuarios;
import cetys.biblioteca.usuarios.TipoUsuario;
import cetys.biblioteca.usuarios.Usuario;
import java.time.LocalDate;

/**
 * Demostracion del FLUJO COMPLETO (Seccion 4):
 *
 *   "Un estudiante solicita un prestamo y el sistema cobra la fianza al
 *    Sistema de Pagos Bancario."
 *
 * Esta clase integra los 4 patrones de la Seccion 2 + Clean Architecture
 * de la Seccion 3, y permite ver en consola como interactuan todos los
 * componentes en orden cronologico.
 *
 * Patrones que se ejercitan:
 *   - FACTORY:   crea el Usuario al inicio (paso 0)
 *   - BUILDER:   construye SolicitudPrestamo inmutable (paso 1)
 *   - ADAPTER:   CatalogoCETYSAdapter consulta SOAP (paso 4 dentro del UC)
 *   - SINGLETON: AuditoriaLogger registra el evento (paso 8 dentro del UC)
 *
 * Capas Clean Architecture que participan:
 *   - DOMINIO:        Prestamo, RepositorioPrestamos, ServicioNotificacion, PagosGateway
 *   - APLICACION:     RegistrarPrestamoUseCase
 *   - INFRAESTRUCTURA: RepoPrestamosEnMemoria, ServicioNotificacionConsola,
 *                     PagosGatewaySimulado, CatalogoCETYSAdapter
 */
public class DemoFlujoCompleto {

    public static void main(String[] args) {
        System.out.println("=== Demo Flujo Completo: prestamo + cobro de fianza ===\n");

        // -----------------------------------------------------------
        // CABLEADO (composition root): instanciar adaptadores y use case.
        // En produccion (Spring) esto seria una clase @Configuration
        // con metodos @Bean que retornan estas instancias.
        // -----------------------------------------------------------
        CatalogoBiblioteca catalogo = new CatalogoCETYSAdapter(new CatalogoCETYS());
        RepositorioPrestamos repo   = new RepoPrestamosEnMemoria();
        ServicioNotificacion notif  = new ServicioNotificacionConsola();
        PagosGateway pagos          = new PagosGatewaySimulado();

        RegistrarPrestamoUseCase useCase =
            new RegistrarPrestamoUseCase(repo, catalogo, notif, pagos);

        // -----------------------------------------------------------
        // PASO 0: FACTORY - crear el estudiante (en sesion previa)
        // -----------------------------------------------------------
        System.out.println(">> [FACTORY] Creando Usuario via FabricaDeUsuarios");
        FabricaDeUsuarios fabrica = new FabricaDeUsuarios();
        Usuario usuario = fabrica.crear(TipoUsuario.ESTUDIANTE,
                                         "CT-2024-001", "Betsy Danaeth Arceo Rivera");
        System.out.printf("   Usuario creado: %s (%s)%n%n",
            usuario.getNombre(), usuario.getRol());

        // -----------------------------------------------------------
        // PASO 1: BUILDER - construir la solicitud inmutable
        // -----------------------------------------------------------
        System.out.println(">> [BUILDER] Construyendo SolicitudPrestamo");
        Libro libroAux = new Libro("978-0134494166", "Clean Architecture",
                                   "Robert C. Martin", 2017, true);
        SolicitudPrestamo solicitud = new SolicitudPrestamoBuilder()
            .conEstudiante((cetys.biblioteca.usuarios.Estudiante) usuario)
            .conLibro(libroAux)
            .conFechaDevolucion(LocalDate.now().plusDays(14))
            .conNotasEspeciales("Material para tesis")
            .construir();
        System.out.println("   Solicitud: " + solicitud + "\n");

        // -----------------------------------------------------------
        // PASO 2: USE CASE - ejecutar el flujo completo
        // (dentro hay Adapter, Singleton, repositorio, gateway, notif)
        // -----------------------------------------------------------
        System.out.println(">> [USE CASE] Ejecutando RegistrarPrestamoUseCase");
        Prestamo prestamo = useCase.ejecutar(solicitud);

        System.out.printf("%n>> Resultado: prestamo id=%s para %s%n",
            prestamo.getId(), prestamo.getUsuario().getNombre());
        System.out.println("\n[OK] Flujo completo ejecutado exitosamente.");
        System.out.println("     Los 4 patrones de la Seccion 2 actuaron en este flujo:");
        System.out.println("     - Factory:   creo el Usuario");
        System.out.println("     - Builder:   construyo SolicitudPrestamo inmutable");
        System.out.println("     - Adapter:   tradujo el llamado al Catalogo CETYS SOAP");
        System.out.println("     - Singleton: registro el evento en AuditoriaLogger");
    }
}
