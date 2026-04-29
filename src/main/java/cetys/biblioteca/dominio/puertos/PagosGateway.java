package cetys.biblioteca.dominio.puertos;

/**
 * Puerto del dominio para cobrar fianzas y multas via el Sistema de
 * Pagos Bancario.
 *
 * El use case (RegistrarPrestamoUseCase) solo conoce esta interfaz.
 * El adaptador concreto (REST, gRPC, mock para tests) vive en infraestructura.
 */
public interface PagosGateway {

    /**
     * Cobra una cantidad al usuario via el Sistema de Pagos.
     *
     * @param usuarioId identificador del usuario
     * @param monto     cantidad a cobrar (en MXN)
     * @return identificador del recibo emitido por el banco
     */
    String cobrarFianza(String usuarioId, double monto);
}
