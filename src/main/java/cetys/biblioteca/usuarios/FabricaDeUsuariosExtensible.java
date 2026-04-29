package cetys.biblioteca.usuarios;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Fabrica EXTENSIBLE que cumple estrictamente el Open/Closed Principle.
 *
 * En lugar de un switch que debe modificarse al agregar tipos, esta fabrica
 * mantiene un Map de constructores que se pueden REGISTRAR en tiempo de
 * ejecucion. Para agregar un nuevo tipo (ej. Posgrado):
 *
 *   1. Crear la clase Posgrado implements Usuario  (CODIGO NUEVO)
 *   2. fabrica.registrar("POSGRADO", Posgrado::new) (CONFIGURACION)
 *
 * En NINGUN paso se modifica esta clase ni las existentes.
 *
 * Esto materializa los principios:
 *   - OCP: abierta a extension, cerrada a modificacion.
 *   - LSP: cualquier Usuario es sustituible.
 *   - DIP: el cliente depende de la abstraccion Usuario.
 */
public class FabricaDeUsuariosExtensible {

    private final Map<String, BiFunction<String, String, Usuario>> constructores = new HashMap<>();

    /**
     * Registra los tipos por defecto. Tipos adicionales se registran
     * desde la configuracion (ej. una clase @Configuration de Spring).
     */
    public FabricaDeUsuariosExtensible() {
        registrar("ESTUDIANTE",    Estudiante::new);
        registrar("BIBLIOTECARIO", Bibliotecario::new);
        registrar("ADMIN",         Admin::new);
    }

    /**
     * Registra un constructor para un nuevo tipo de usuario.
     *
     * @param tipo        nombre del tipo (case-insensitive)
     * @param constructor referencia al constructor (BiFunction id, nombre -> Usuario)
     */
    public void registrar(String tipo, BiFunction<String, String, Usuario> constructor) {
        constructores.put(tipo.toUpperCase(), constructor);
    }

    /**
     * Crea una instancia del tipo solicitado.
     *
     * @throws IllegalArgumentException si el tipo no esta registrado.
     */
    public Usuario crear(String tipo, String id, String nombre) {
        var ctor = constructores.get(tipo.toUpperCase());
        if (ctor == null) {
            throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipo);
        }
        return ctor.apply(id, nombre);
    }
}
