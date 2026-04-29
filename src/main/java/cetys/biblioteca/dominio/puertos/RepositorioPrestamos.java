package cetys.biblioteca.dominio.puertos;

import cetys.biblioteca.dominio.modelo.Prestamo;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (output port) que define el contrato de persistencia
 * de Prestamo.
 *
 * UBICACION: capa de DOMINIO (no infraestructura). Esta es la idea central
 * de la Dependency Inversion: el dominio "es dueno" del contrato; las
 * implementaciones concretas (JPA, MongoDB, en memoria) viven en la capa
 * externa y dependen del dominio al implementar esta interfaz.
 *
 * Vocabulario: la interfaz habla de Prestamo (concepto del dominio), no de
 * filas, columnas o documentos. Si manana cambia el motor de persistencia,
 * el lenguaje de la interfaz no cambia.
 */
public interface RepositorioPrestamos {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorId(String id);

    List<Prestamo> buscarPorUsuario(String usuarioId);

    List<Prestamo> buscarActivos();
}
