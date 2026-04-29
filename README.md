# Examen de prueba — Sistema de Biblioteca Universitaria

**Materia:** Arquitectura de Software
**Institución:** CETYS Universidad
**Tipo:** Parcial práctico
**Temas evaluados:** Modelo C4 · Singleton · Factory · Adapter · Builder · Principios SOLID · Clean Architecture

---

## Caso de estudio

CETYS Universidad necesita un sistema para gestionar préstamos de libros, reservas de salas de estudio y multas a estudiantes. El sistema debe integrarse con tres fuentes externas:

- **Catálogo CETYS** — expone libros vía SOAP con su propio esquema de datos
- **Sistema de Pagos Bancario** — API REST con firma diferente a la esperada internamente
- **Directorio Estudiantil Institucional** — sistema LDAP de autenticación

Toda acción del usuario debe quedar en un **único registro de auditoría centralizado**. Además, el sistema debe ser extensible: en el futuro se agregarán nuevos tipos de usuario (posgrado, externos, profesores) **sin tocar el código existente**.

---

## Índice de respuestas

| Sección | Tema | Puntos | Carpeta |
|---------|------|--------|---------|
| 1 | Modelo C4 | 30 | [seccion-1-modelo-c4/](./seccion-1-modelo-c4/) |
| 2 | Patrones de diseño | 40 | [seccion-2-patrones-diseno/](./seccion-2-patrones-diseno/) |
| 3 | SOLID + Clean Architecture | 20 | [seccion-3-solid-clean-architecture/](./seccion-3-solid-clean-architecture/) |
| 4 | Síntesis integradora | 10 | [seccion-4-integracion/](./seccion-4-integracion/) |
| **Total** | | **100** | |

---

## Stack tecnológico propuesto

- **Lenguaje:** Java 17
- **Framework:** Spring Boot (mencionado en arquitectura; los demos son Java puro compilable con Maven)
- **IDE:** IntelliJ IDEA
- **Diagramas C4:** [Structurizr DSL](https://structurizr.com/dsl)
- **Diagramas UML:** [Mermaid](https://mermaid.live)
- **Build:** Maven 3.9+

---

## Estructura del repositorio

```
examen-arquitectura-biblioteca/
├── README.md                         ← este archivo (portada e índice)
├── pom.xml                           ← configuración Maven
├── .gitignore
│
├── seccion-1-modelo-c4/              ← respuestas teóricas Sección 1
│   ├── 1A-diagrama-contexto.md
│   ├── 1B-diagrama-contenedores.md
│   ├── 1C-diagrama-componentes.md
│   └── workspace.dsl                 ← código DSL de Structurizr
│
├── seccion-2-patrones-diseno/        ← respuestas teóricas Sección 2
│   ├── 2A-singleton/README.md
│   ├── 2B-factory/README.md
│   ├── 2C-adapter/README.md
│   └── 2D-builder/README.md
│
├── seccion-3-solid-clean-architecture/
│   ├── 3A-analisis-violaciones.md
│   └── 3B-use-cases.md
│
├── seccion-4-integracion/
│   └── 4-flujo-completo.md
│
├── diagramas/
│   ├── mermaid/                      ← código fuente de cada diagrama (.mmd)
│   └── png/                          ← exportar aquí desde mermaid.live
│
└── src/main/java/cetys/biblioteca/   ← código fuente Java (Maven)
    ├── auditoria/                    ← Singleton (2A)
    ├── usuarios/                     ← Factory (2B)
    ├── catalogo/                     ← Adapter (2C)
    ├── prestamos/                    ← Builder (2D)
    ├── dominio/                      ← Clean Architecture: dominio
    │   ├── modelo/
    │   └── puertos/                  ← interfaces
    ├── aplicacion/                   ← Clean Architecture: use cases (3B)
    ├── infraestructura/              ← Clean Architecture: adaptadores
    └── demos/                        ← clases ejecutables de demostración
```

---

## Cómo compilar y ejecutar

```bash
# Compilar todo
mvn clean compile

# Ejecutar una demo específica (ejemplo: Singleton)
mvn exec:java -Dexec.mainClass="cetys.biblioteca.demos.DemoSingleton"

# Las clases de demostración disponibles son:
#   cetys.biblioteca.demos.DemoSingleton
#   cetys.biblioteca.demos.DemoFactory
#   cetys.biblioteca.demos.DemoAdapter
#   cetys.biblioteca.demos.DemoBuilder
```

---

## Cómo ver los diagramas

### Diagramas C4 (Sección 1)

1. Abre [https://structurizr.com/dsl](https://structurizr.com/dsl)
2. Pega el contenido de [`seccion-1-modelo-c4/workspace.dsl`](./seccion-1-modelo-c4/workspace.dsl)
3. Selecciona la vista deseada: Contexto, Contenedores, Componentes_API
4. Exporta como PNG y colócala en `diagramas/png/` con el nombre indicado en cada README

### Diagramas UML (Secciones 2, 3, 4)

1. Abre [https://mermaid.live](https://mermaid.live)
2. Copia el contenido del archivo `.mmd` correspondiente desde `diagramas/mermaid/`
3. Exporta como PNG (botón Actions → PNG) y colócala en `diagramas/png/`

GitHub renderiza Mermaid de forma nativa en archivos `.md`, así que los diagramas también aparecen embebidos al navegar el repositorio en línea.

---

## Decisiones arquitectónicas globales

1. **Auditoría centralizada como Singleton.** Modela explícitamente la restricción del rector: una sola instancia en toda la JVM.
2. **Catálogo CETYS detrás de un Adapter.** Aísla al sistema interno de la API SOAP externa.
3. **Use cases independientes de la BD y el framework.** Aplicando la Dependency Rule de Clean Architecture, un cambio de MySQL a MongoDB no afecta la lógica de negocio.
4. **Extensibilidad de tipos de usuario vía Factory + Open/Closed.** Agregar un nuevo tipo (Posgrado) requiere crear una clase nueva, no modificar las existentes.
5. **Solicitudes de préstamo inmutables vía Builder.** Garantiza trazabilidad y elimina race conditions en el procesamiento concurrente.

---

## Autora

Betsy López — CETYS Universidad
