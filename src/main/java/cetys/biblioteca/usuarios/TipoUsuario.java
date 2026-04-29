package cetys.biblioteca.usuarios;

/**
 * Enumeracion de los tipos de usuario soportados por la fabrica clasica.
 *
 * Nota: la version extensible de la fabrica usa String en lugar de este enum
 * para permitir registrar nuevos tipos en tiempo de ejecucion sin recompilar.
 */
public enum TipoUsuario {
    ESTUDIANTE,
    BIBLIOTECARIO,
    ADMIN
}
