package cetys.biblioteca.infraestructura.persistencia;

import cetys.biblioteca.dominio.modelo.Prestamo;
import cetys.biblioteca.dominio.puertos.RepositorioPrestamos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementacion EN MEMORIA del RepositorioPrestamos. Util para demos
 * y tests; en produccion se reemplazaria por RepoPrestamosJPA, sin
 * tocar el dominio ni los use cases.
 *
 * Demuestra la Dependency Rule:
 *   - Esta clase IMPLEMENTA una interfaz del dominio.
 *   - La flecha de dependencia apunta HACIA EL DOMINIO.
 *   - El use case nunca conoce esta clase: solo conoce la interfaz.
 *
 * Tambien es un ejemplo del patron ADAPTER aplicado a persistencia:
 * adapta la API estandar de Java (List, HashMap) al contrato del dominio.
 */
public class RepoPrestamosEnMemoria implements RepositorioPrestamos {

    private final Map<String, Prestamo> almacen = new HashMap<>();

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        almacen.put(prestamo.getId(), prestamo);
        return prestamo;
    }

    @Override
    public Optional<Prestamo> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id));
    }

    @Override
    public List<Prestamo> buscarPorUsuario(String usuarioId) {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : almacen.values()) {
            if (p.getUsuario().getId().equals(usuarioId)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public List<Prestamo> buscarActivos() {
        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : almacen.values()) {
            if (!p.isDevuelto()) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
