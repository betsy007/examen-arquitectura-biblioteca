package cetys.biblioteca.usuarios;

/**
 * Fabrica clasica que encapsula la creacion de objetos Usuario.
 * El codigo cliente no conoce las clases concretas; solo pide un tipo.
 *
 * Limitacion conocida: agregar un nuevo tipo (ej. Posgrado) requiere
 * MODIFICAR el switch de esta clase, lo cual viola el Open/Closed Principle.
 * Para una version que cumple OCP estrictamente ver FabricaDeUsuariosExtensible.
 */
public class FabricaDeUsuarios {

    public Usuario crear(TipoUsuario tipo, String id, String nombre) {
        return switch (tipo) {
            case ESTUDIANTE    -> new Estudiante(id, nombre);
            case BIBLIOTECARIO -> new Bibliotecario(id, nombre);
            case ADMIN         -> new Admin(id, nombre);
        };
    }
}
