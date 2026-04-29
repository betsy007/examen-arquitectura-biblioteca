package cetys.biblioteca.aplicacion;

import cetys.biblioteca.auditoria.AuditoriaLogger;
import cetys.biblioteca.catalogo.CatalogoBiblioteca;
import cetys.biblioteca.catalogo.Libro;
import cetys.biblioteca.dominio.modelo.Prestamo;
import cetys.biblioteca.dominio.puertos.PagosGateway;
import cetys.biblioteca.dominio.puertos.RepositorioPrestamos;
import cetys.biblioteca.dominio.puertos.ServicioNotificacion;
import cetys.biblioteca.prestamos.SolicitudPrestamo;

/**
 * Caso de uso: registrar un prestamo de libro.
 *
 * RESPONSABILIDADES:
 *   1. Verificar disponibilidad del libro consultando el catalogo.
 *   2. Validar que el estudiante no exceda su limite de prestamos.
 *   3. Cobrar la fianza al Sistema de Pagos Bancario.
 *   4. Persistir el prestamo via el repositorio.
 *   5. Notificar al estudiante.
 *   6. Registrar en auditoria centralizada.
 *
 * DEPENDENCIAS:
 *   Este use case depende SOLO de abstracciones (puertos del dominio).
 *   No conoce JPA, MongoDB, SMTP, SOAP ni Spring REST.
 *   Esto es exactamente lo que la Dependency Rule de Clean Architecture
 *   exige: las capas internas no dependen de detalles externos.
 *
 * INTEGRA LOS 4 PATRONES DE LA SECCION 2:
 *   - Builder:   recibe SolicitudPrestamo (construida via Builder).
 *   - Adapter:   accede al catalogo via la interfaz CatalogoBiblioteca,
 *                cuya implementacion concreta es CatalogoCETYSAdapter.
 *   - Singleton: registra eventos via AuditoriaLogger.getInstancia().
 *   - Factory:   el Estudiante en la solicitud fue creado previamente
 *                por FabricaDeUsuarios durante la autenticacion.
 */
public class RegistrarPrestamoUseCase {

    private final RepositorioPrestamos repoPrestamos;
    private final CatalogoBiblioteca   catalogo;
    private final ServicioNotificacion notificacion;
    private final PagosGateway         pagosGateway;

    private static final double MONTO_FIANZA_DEFAULT = 100.0;

    /** Constructor Injection: dependencias inyectadas, no instanciadas. */
    public RegistrarPrestamoUseCase(RepositorioPrestamos repoPrestamos,
                                    CatalogoBiblioteca catalogo,
                                    ServicioNotificacion notificacion,
                                    PagosGateway pagosGateway) {
        this.repoPrestamos = repoPrestamos;
        this.catalogo = catalogo;
        this.notificacion = notificacion;
        this.pagosGateway = pagosGateway;
    }

    public Prestamo ejecutar(SolicitudPrestamo solicitud) {

        // 1. Verificar disponibilidad consultando el catalogo (via Adapter).
        Libro libro = catalogo.buscarLibro(solicitud.getLibro().getIsbn());
        if (!libro.isDisponible()) {
            throw new IllegalStateException(
                "Libro no disponible: " + libro.getTitulo()
            );
        }

        // 2. Validar regla de negocio: limite de prestamos activos.
        long prestamosActivos = repoPrestamos
            .buscarPorUsuario(solicitud.getEstudiante().getId())
            .stream()
            .filter(p -> !p.isDevuelto())
            .count();

        if (prestamosActivos >= solicitud.getEstudiante().getLimitePrestamos()) {
            throw new IllegalStateException(
                "El estudiante alcanzo su limite de prestamos activos"
            );
        }

        // 3. Cobrar fianza al Sistema de Pagos Bancario.
        String reciboId = pagosGateway.cobrarFianza(
            solicitud.getEstudiante().getId(),
            MONTO_FIANZA_DEFAULT
        );

        // 4. Crear y persistir el prestamo (via repositorio, no JPA directo).
        Prestamo prestamo = new Prestamo(
            solicitud.getEstudiante(),
            libro,
            solicitud.getFechaDevolucion()
        );
        Prestamo guardado = repoPrestamos.guardar(prestamo);

        // 5. Notificar al estudiante (via servicio, no SMTP directo).
        notificacion.notificar(
            solicitud.getEstudiante().getId(),
            "Prestamo registrado",
            "Se registro el prestamo de: " + libro.getTitulo()
                + ". Recibo de fianza: " + reciboId
        );

        // 6. Auditoria centralizada (Singleton).
        AuditoriaLogger.getInstancia().registrar(
            "PRESTAMO_REGISTRADO",
            solicitud.getEstudiante().getId()
        );

        return guardado;
    }
}
