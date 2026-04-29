package cetys.biblioteca.usuarios;

/**
 * Implementacion concreta de Usuario para administradores del sistema.
 * Sin limite practico de prestamos (Integer.MAX_VALUE).
 */
public class Admin implements Usuario {

    private final String id;
    private final String nombre;

    public Admin(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override public String getId()              { return id; }
    @Override public String getNombre()          { return nombre; }
    @Override public String getRol()             { return "ADMIN"; }
    @Override public int    getLimitePrestamos() { return Integer.MAX_VALUE; }
    @Override public boolean puedeAprobarPrestamos() { return true; }
}
