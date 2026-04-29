# Pregunta 2B — Factory · Creación de usuarios *(10 pts)*

## Enunciado

> El sistema debe crear objetos de tipo Usuario (Estudiante, Bibliotecario, Admin) sin que el código cliente conozca las clases concretas. Implementa una FabricaDeUsuarios y:
>
> 1. Define la interfaz Usuario con métodos comunes.
> 2. Implementa las tres clases concretas.
> 3. Dibuja el diagrama de clases UML con la fábrica y las implementaciones.
> 4. Muestra cómo agregar el tipo Posgrado en el futuro sin modificar el código existente. ¿Qué principio SOLID garantiza esto?

## Solución

### Código

| Archivo | Rol |
|---------|-----|
| [`Usuario.java`](../../src/main/java/cetys/biblioteca/usuarios/Usuario.java) | Interfaz común |
| [`Estudiante.java`](../../src/main/java/cetys/biblioteca/usuarios/Estudiante.java) | Implementación |
| [`Bibliotecario.java`](../../src/main/java/cetys/biblioteca/usuarios/Bibliotecario.java) | Implementación |
| [`Admin.java`](../../src/main/java/cetys/biblioteca/usuarios/Admin.java) | Implementación |
| [`Posgrado.java`](../../src/main/java/cetys/biblioteca/usuarios/Posgrado.java) | Extensión futura |
| [`TipoUsuario.java`](../../src/main/java/cetys/biblioteca/usuarios/TipoUsuario.java) | Enum de tipos |
| [`FabricaDeUsuarios.java`](../../src/main/java/cetys/biblioteca/usuarios/FabricaDeUsuarios.java) | Factory clásica |
| [`FabricaDeUsuariosExtensible.java`](../../src/main/java/cetys/biblioteca/usuarios/FabricaDeUsuariosExtensible.java) | Factory que cumple OCP |
| [`DemoFactory.java`](../../src/main/java/cetys/biblioteca/demos/DemoFactory.java) | Demo ejecutable |

### 1. Interfaz Usuario

```java
public interface Usuario {
    String getId();
    String getNombre();
    String getRol();
    int getLimitePrestamos();
    boolean puedeAprobarPrestamos();
}
```

Esta interfaz define el **contrato común** que todo tipo de usuario debe cumplir. El código cliente (controllers, use cases) depende SOLO de esta abstracción.

### 2. Clases concretas

Las tres clases implementan `Usuario` con valores específicos:

| Tipo | Límite de préstamos | Aprueba préstamos |
|------|---------------------|-------------------|
| Estudiante | 3 | No |
| Bibliotecario | 10 | Sí |
| Admin | Sin límite | Sí |

### 3. Diagrama UML

Ver [`uml-factory.mmd`](./uml-factory.mmd) y la versión renderizada en [`uml-factory.png`](./uml-factory.png).

### 4. Agregar Posgrado sin modificar código existente

Se incluyen dos versiones de la fábrica:

#### Versión clásica (con switch)

```java
public class FabricaDeUsuarios {
    public Usuario crear(TipoUsuario tipo, String id, String nombre) {
        return switch (tipo) {
            case ESTUDIANTE    -> new Estudiante(id, nombre);
            case BIBLIOTECARIO -> new Bibliotecario(id, nombre);
            case ADMIN         -> new Admin(id, nombre);
        };
    }
}
```

**Limitación honesta:** agregar Posgrado a esta versión *sí* requiere modificar el método `crear()`, lo cual viola estrictamente el OCP.

#### Versión extensible (registro dinámico)

```java
public class FabricaDeUsuariosExtensible {
    private final Map<String, BiFunction<String, String, Usuario>> constructores = new HashMap<>();
    
    public FabricaDeUsuariosExtensible() {
        registrar("ESTUDIANTE",    Estudiante::new);
        registrar("BIBLIOTECARIO", Bibliotecario::new);
        registrar("ADMIN",         Admin::new);
    }
    
    public void registrar(String tipo, BiFunction<String, String, Usuario> ctor) {
        constructores.put(tipo.toUpperCase(), ctor);
    }
    
    public Usuario crear(String tipo, String id, String nombre) {
        return constructores.get(tipo.toUpperCase()).apply(id, nombre);
    }
}
```

#### Cómo agregar Posgrado

**Solo se crea una clase nueva** (`Posgrado.java`) y se registra. Cero modificaciones a archivos existentes:

```java
var fabrica = new FabricaDeUsuariosExtensible();
fabrica.registrar("POSGRADO", Posgrado::new);

Usuario u = fabrica.crear("POSGRADO", "MA-001", "Betsy Danaeth Arceo Rivera");
```

#### Principio SOLID que lo garantiza

**Open/Closed Principle (OCP):**

> *"Las entidades de software deben estar **abiertas a extensión** pero **cerradas a modificación**."*

En la versión extensible, agregar Posgrado requirió:

1. **Crear** un archivo nuevo (`Posgrado.java`) que implementa la interfaz `Usuario`. Esto es *extensión*.
2. **Registrar** el constructor en el contenedor de Spring o donde se inicialice la fábrica. La fábrica no se modifica.

**Principios secundarios que también aplican:**

- **Liskov Substitution Principle (LSP):** cualquier `Usuario` (Estudiante, Bibliotecario, Admin, Posgrado) puede usarse donde el código espera un `Usuario` sin romper el comportamiento.
- **Dependency Inversion Principle (DIP):** los controllers y use cases dependen de la abstracción `Usuario`, no de las clases concretas. La fábrica es el único punto que conoce los tipos reales.

## Cómo ejecutar la demo

```bash
mvn compile
java -cp target/classes cetys.biblioteca.demos.DemoFactory
```

**Salida esperada:**

```
=== Demo Factory: FabricaDeUsuarios ===

ESTUDIANTE [ES-001] - Betsy Danaeth Arceo Rivera | límite: 3 | aprueba: false
BIBLIOTECARIO [BB-001] - María García | límite: 10 | aprueba: true
ADMIN [AD-001] - Carlos Méndez | límite: 2147483647 | aprueba: true

=== Versión extensible: agregar Posgrado en caliente ===

POSGRADO [PG-001] - Ana Ruiz | límite: 6 | aprueba: false
```
