# Pregunta 2D — Builder · Solicitudes de préstamo *(10 pts)*

## Enunciado

> Una `SolicitudPrestamo` tiene los siguientes atributos:
>
> | Atributo | Tipo | ¿Obligatorio? | Default |
> |----------|------|---------------|---------|
> | estudiante | Estudiante | Sí | — |
> | libro | Libro | Sí | — |
> | fechaDevolucion | Date | Sí | — |
> | notasEspeciales | String | No | null |
> | renovacionAutomatica | boolean | No | false |
> | numRenovaciones | int | No | 1 |
>
> Con base en esto:
>
> 1. Implementa un SolicitudPrestamoBuilder con métodos encadenables.
> 2. El método construir() debe validar que los campos obligatorios estén presentes antes de crear el objeto.
> 3. Muestra tres ejemplos de uso con distintas combinaciones de atributos opcionales.
> 4. Reflexión: ¿por qué conviene que SolicitudPrestamo sea inmutable una vez construida? ¿Qué problemas evitamos?

## Solución

### Código

| Archivo | Rol |
|---------|-----|
| [`SolicitudPrestamo.java`](../../src/main/java/cetys/biblioteca/prestamos/SolicitudPrestamo.java) | Clase inmutable resultado |
| [`SolicitudPrestamoBuilder.java`](../../src/main/java/cetys/biblioteca/prestamos/SolicitudPrestamoBuilder.java) | Builder con fluent interface |
| [`DemoBuilder.java`](../../src/main/java/cetys/biblioteca/demos/DemoBuilder.java) | Demo ejecutable con tres casos + validación |

### 1. Builder con métodos encadenables

```java
public class SolicitudPrestamoBuilder {
    Estudiante estudiante;
    Libro libro;
    LocalDate fechaDevolucion;
    String notasEspeciales = null;
    boolean renovacionAutomatica = false;
    int numRenovaciones = 1;
    
    public SolicitudPrestamoBuilder conEstudiante(Estudiante e) {
        this.estudiante = e;
        return this;  // habilita encadenamiento
    }
    // ... resto de métodos análogos
    
    public SolicitudPrestamo construir() { /* valida y crea */ }
}
```

Cada método setter retorna `this`, permitiendo el estilo fluent:

```java
new SolicitudPrestamoBuilder()
    .conEstudiante(betsy)
    .conLibro(cleanArch)
    .conFechaDevolucion(fecha)
    .construir();
```

### 2. Validación en `construir()`

El método valida **todos** los obligatorios + reglas de negocio antes de instanciar, y **acumula los errores** en lugar de fallar al primero:

```java
public SolicitudPrestamo construir() {
    List<String> errores = new ArrayList<>();
    if (estudiante == null)       errores.add("estudiante es obligatorio");
    if (libro == null)            errores.add("libro es obligatorio");
    if (fechaDevolucion == null)  errores.add("fechaDevolucion es obligatoria");
    else if (fechaDevolucion.isBefore(LocalDate.now()))
        errores.add("fechaDevolucion no puede estar en el pasado");
    if (numRenovaciones < 0)      errores.add("numRenovaciones no puede ser negativo");

    if (!errores.isEmpty()) {
        throw new IllegalStateException(
            "No se puede construir SolicitudPrestamo: " + String.join("; ", errores));
    }
    return new SolicitudPrestamo(this);
}
```

### 3. Tres ejemplos de uso

```java
// Ejemplo 1: solo campos obligatorios
SolicitudPrestamo s1 = new SolicitudPrestamoBuilder()
    .conEstudiante(betsy)
    .conLibro(cleanArch)
    .conFechaDevolucion(LocalDate.now().plusDays(14))
    .construir();

// Ejemplo 2: con notas especiales y renovación automática
SolicitudPrestamo s2 = new SolicitudPrestamoBuilder()
    .conEstudiante(betsy)
    .conLibro(pragmatic)
    .conFechaDevolucion(LocalDate.now().plusDays(21))
    .conNotasEspeciales("Material para tesis - prioridad alta")
    .conRenovacionAutomatica(true)
    .construir();

// Ejemplo 3: con número de renovaciones personalizado
SolicitudPrestamo s3 = new SolicitudPrestamoBuilder()
    .conEstudiante(betsy)
    .conLibro(dddBook)
    .conFechaDevolucion(LocalDate.now().plusDays(30))
    .conNumRenovaciones(3)
    .construir();
```

Ver código completo en [`DemoBuilder.java`](../../src/main/java/cetys/biblioteca/demos/DemoBuilder.java).

### Diagrama UML

Ver [`uml-builder.mmd`](./uml-builder.mmd) y la versión renderizada en [`uml-builder.png`](./uml-builder.png).

### 4. Reflexión: ¿por qué inmutable?

Una solicitud de préstamo es un **registro contractual**: representa el acuerdo entre el estudiante y la biblioteca. Igual que en un sistema bancario una transacción no se modifica después de ejecutarse, una solicitud aceptada no debe cambiar. Hacerla inmutable formaliza esta regla en el código.

#### Problemas que evitamos con la inmutabilidad

1. **Race conditions en concurrencia.** Múltiples hilos pueden leer la misma `SolicitudPrestamo` simultáneamente sin sincronización. Si fuera mutable, dos hilos podrían leer un estado intermedio y tomar decisiones inconsistentes.

2. **Auditoría falsificable.** El `AuditoriaLogger` (Singleton) registra "se aprobó la solicitud X con fecha Y". Si la solicitud fuera mutable, alguien podría cambiar la fecha *después* del log y el registro de auditoría dejaría de coincidir con el estado real.

3. **Bugs por aliasing.** Si la solicitud se pasa al UseCase, al PagosGateway y al RepositorioPrestamos, cualquiera de ellos podría mutarla y los demás verían un estado distinto del esperado.

4. **Validación que se vuelve mentira.** El builder valida en `construir()` que la fecha no esté en el pasado y que los obligatorios estén presentes. Si después alguien hace `solicitud.setEstudiante(null)`, esa validación se invalida. Sin setters, la validación garantiza el invariante para *toda la vida del objeto*.

5. **Hash codes inestables en colecciones.** Si guardáramos `SolicitudPrestamo` en un `HashSet` y mutáramos un campo, el `hashCode` cambiaría y el objeto se "perdería" dentro de la colección.

#### Cómo se "modifica" si se necesita cambiar algo

Si una solicitud necesita actualizarse (ej. extender la fecha de devolución), se crea una **nueva** solicitud derivada de la anterior:

```java
SolicitudPrestamo extendida = new SolicitudPrestamoBuilder()
    .conEstudiante(original.getEstudiante())
    .conLibro(original.getLibro())
    .conFechaDevolucion(original.getFechaDevolucion().plusDays(7))
    .conNotasEspeciales(original.getNotasEspeciales())
    .conRenovacionAutomatica(original.isRenovacionAutomatica())
    .conNumRenovaciones(original.getNumRenovaciones() + 1)
    .construir();
```

### Comparación con alternativas

| Alternativa | Problema |
|---|---|
| Constructor con 6 parámetros | Difícil de leer, parámetros booleanos sin contexto, errores fáciles al ordenar |
| Constructor + setters (JavaBean) | Permite objetos a medio construir, viola la inmutabilidad |
| Múltiples constructores sobrecargados | Explosión combinatoria: con 3 opcionales → 8 constructores posibles |
| **Builder** | Lectura natural, valida antes de instanciar, soporta opcionales con defaults, produce objeto inmutable |

## Cómo ejecutar la demo

```bash
mvn compile
java -cp target/classes cetys.biblioteca.demos.DemoBuilder
```
