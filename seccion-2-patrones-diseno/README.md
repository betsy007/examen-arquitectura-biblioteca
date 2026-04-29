# Sección 2 — Patrones de diseño *(40 pts)*

Esta sección implementa los cuatro patrones de diseño aplicados al sistema de Biblioteca CETYS. Cada subpregunta tiene su propia carpeta con el README de respuestas, el diagrama Mermaid (`.mmd`), su versión renderizada (`.png`) y enlaces al código en `src/main/java/`.

## Subpreguntas

| Subpregunta | Patrón | Carpeta | Componente |
|-------------|--------|---------|------------|
| 2A *(10 pts)* | **Singleton** | [2A-singleton/](./2A-singleton/) | `AuditoriaLogger` |
| 2B *(10 pts)* | **Factory** | [2B-factory/](./2B-factory/) | `FabricaDeUsuarios` |
| 2C *(10 pts)* | **Adapter** | [2C-adapter/](./2C-adapter/) | `CatalogoCETYSAdapter` |
| 2D *(10 pts)* | **Builder** | [2D-builder/](./2D-builder/) | `SolicitudPrestamoBuilder` |

## Resumen de los cuatro patrones

| Patrón | Categoría GoF | Problema que resuelve |
|--------|---------------|------------------------|
| Singleton | Creacional | Garantizar una única instancia del logger para auditoría centralizada |
| Factory | Creacional | Crear usuarios sin que el cliente conozca clases concretas, permitir extensión a Posgrado |
| Adapter | Estructural | Reconciliar interfaz interna `CatalogoBiblioteca` con API externa SOAP del catálogo CETYS |
| Builder | Creacional | Construir `SolicitudPrestamo` inmutable con muchos campos opcionales y validación |

## Cómo compilar y ejecutar todos los demos

```bash
# Desde la raíz del proyecto
mvn compile

# Ejecutar cada demo individualmente
java -cp target/classes cetys.biblioteca.demos.DemoSingleton
java -cp target/classes cetys.biblioteca.demos.DemoFactory
java -cp target/classes cetys.biblioteca.demos.DemoAdapter
java -cp target/classes cetys.biblioteca.demos.DemoBuilder
```

## Estructura del código de Sección 2

```
src/main/java/cetys/biblioteca/
├── auditoria/              ← Singleton (2A)
│   ├── AuditoriaLogger.java
│   └── AuditoriaLoggerEnum.java
├── usuarios/               ← Factory (2B)
│   ├── Usuario.java
│   ├── Estudiante.java
│   ├── Bibliotecario.java
│   ├── Admin.java
│   ├── Posgrado.java
│   ├── TipoUsuario.java
│   ├── FabricaDeUsuarios.java
│   └── FabricaDeUsuariosExtensible.java
├── catalogo/               ← Adapter (2C)
│   ├── Libro.java
│   ├── CatalogoBiblioteca.java
│   └── adaptadores/
│       ├── ResultadoSOAP.java
│       ├── CatalogoCETYS.java
│       └── CatalogoCETYSAdapter.java
├── prestamos/              ← Builder (2D)
│   ├── SolicitudPrestamo.java
│   └── SolicitudPrestamoBuilder.java
└── demos/                  ← programas main
    ├── DemoSingleton.java
    ├── DemoFactory.java
    ├── DemoAdapter.java
    └── DemoBuilder.java
```
