package cetys.biblioteca.usuarios;

/**
 * Implementacion concreta de Usuario para estudiantes de posgrado.
 *
 * EXTENSION: Esta clase fue agregada DESPUES, sin modificar ninguna
 * clase existente del sistema. Demuestra el cumplimiento del Open/Closed
 * Principle: el sistema esta abierto a extension pero cerrado a modificacion.
 *
 * Limite de prestamos elevado (6) ya que requieren mas material para tesis.
 */
public class Posgrado implements Usuario {

    private final String id;
    private final String nombre;

    public Posgrado(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override public String getId()              { return id; }
    @Override public String getNombre()          { return nombre; }
    @Override public String getRol()             { return "POSGRADO"; }
    @Override public int    getLimitePrestamos() { return 6; }
    @Override public boolean puedeAprobarPrestamos() { return false; }
}
