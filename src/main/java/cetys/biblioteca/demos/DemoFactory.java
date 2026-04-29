package cetys.biblioteca.demos;

import cetys.biblioteca.usuarios.*;

/**
 * Demostracion ejecutable del patron Factory (FabricaDeUsuarios).
 *
 * Demuestra:
 *   1. La fabrica clasica (con switch) creando los 3 tipos basicos.
 *   2. La fabrica extensible cumpliendo OCP: se agrega Posgrado SIN
 *      modificar codigo existente.
 *   3. Que el codigo cliente solo conoce la interfaz Usuario, no las
 *      clases concretas.
 */
public class DemoFactory {

    public static void main(String[] args) {
        System.out.println("=== Demo Factory: FabricaDeUsuarios ===\n");

        // ---- 1. Fabrica clasica ----
        System.out.println("--- Fabrica clasica (con switch) ---");
        FabricaDeUsuarios fabrica = new FabricaDeUsuarios();

        Usuario u1 = fabrica.crear(TipoUsuario.ESTUDIANTE,    "EST-001", "Betsy Danaeth Arceo Rivera");
        Usuario u2 = fabrica.crear(TipoUsuario.BIBLIOTECARIO, "BIB-002", "Carlos Ruiz");
        Usuario u3 = fabrica.crear(TipoUsuario.ADMIN,         "ADM-003", "Pamela Garcia");

        imprimirUsuario(u1);
        imprimirUsuario(u2);
        imprimirUsuario(u3);

        // ---- 2. Fabrica extensible (cumple OCP) ----
        System.out.println("\n--- Fabrica extensible (cumple OCP) ---");
        FabricaDeUsuariosExtensible fabricaExt = new FabricaDeUsuariosExtensible();

        // Sin modificar la fabrica, registramos un nuevo tipo:
        fabricaExt.registrar("POSGRADO", Posgrado::new);

        Usuario u4 = fabricaExt.crear("ESTUDIANTE", "EST-100", "Estudiante Pregrado");
        Usuario u5 = fabricaExt.crear("POSGRADO",   "MA-200",  "Estudiante Posgrado");

        imprimirUsuario(u4);
        imprimirUsuario(u5);

        System.out.println("\n[OK] Posgrado se agrego SIN modificar las clases existentes.");
        System.out.println("     Esto cumple el Open/Closed Principle (OCP).");
    }

    /**
     * Helper que imprime info de un Usuario sin saber su tipo concreto.
     * Demuestra que el codigo cliente depende solo de la interfaz Usuario.
     */
    private static void imprimirUsuario(Usuario u) {
        System.out.printf("  %s | rol=%s | limite=%d | aprueba=%b%n",
            u.getNombre(), u.getRol(), u.getLimitePrestamos(),
            u.puedeAprobarPrestamos());
    }
}
