package cetys.biblioteca.infraestructura.pagos;

import cetys.biblioteca.dominio.puertos.PagosGateway;
import java.util.UUID;

/**
 * Implementacion simulada de PagosGateway que NO hace llamadas REST reales.
 * Util para demos. En produccion se reemplazaria por PagosGatewayREST que
 * usaria Spring WebClient para hablar con el Sistema de Pagos Bancario.
 *
 * Demuestra que el use case NO conoce la diferencia: solo conoce la
 * interfaz PagosGateway.
 */
public class PagosGatewaySimulado implements PagosGateway {

    @Override
    public String cobrarFianza(String usuarioId, double monto) {
        String reciboId = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.printf("  [PAGOS -> simulado] Cobrando $%.2f MXN a %s | recibo=%s%n",
            monto, usuarioId, reciboId);
        return reciboId;
    }
}
