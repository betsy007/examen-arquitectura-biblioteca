package cetys.biblioteca.usuarios;

/**
 * Implementacion concreta de Usuario para estudiantes regulares.
 * Limite de prestamos: 3 simultaneos.
 */
public class Estudiante implements Usuario {

    private final String id;
    private final String nombre;

    public Estudiante(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override public String getId()              { return id; }
    @Override public String getNombre()          { return nombre; }
    @Override public String getRol()             { return "ESTUDIANTE"; }
    @Override public int    getLimitePrestamos() { return 3; }
    @Override public boolean puedeAprobarPrestamos() { return false; }
}
