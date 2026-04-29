package cetys.biblioteca.usuarios;

/**
 * Implementacion concreta de Usuario para bibliotecarios.
 * Tienen permisos elevados: pueden aprobar prestamos.
 */
public class Bibliotecario implements Usuario {

    private final String id;
    private final String nombre;

    public Bibliotecario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override public String getId()              { return id; }
    @Override public String getNombre()          { return nombre; }
    @Override public String getRol()             { return "BIBLIOTECARIO"; }
    @Override public int    getLimitePrestamos() { return 10; }
    @Override public boolean puedeAprobarPrestamos() { return true; }
}
