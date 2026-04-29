workspace "Biblioteca CETYS" "Sistema de gestión de biblioteca universitaria" {

    model {
        # ── Actoraes humanos ──
        estudiante    = person "Estudiante"      "Solicita préstamos, reserva salas y paga multas."
        bibliotecario = person "Bibliotecario"   "Gestiona préstamos, devoluciones y multas."
        admin         = person "Administrador"   "Administra usuarios, políticas y configuración."

        # ── Sistema bajo diseño ──
        biblioteca = softwareSystem "Sistema de Biblioteca CETYS" "Gestiona préstamos, reservas y multas." {
            webapp = container "Web App" "SPA para estudiantes, bibliotecarios y admin." "React + TypeScript" "WebApp"
            api = container "API Backend" "Lógica de negocio: préstamos, reservas, multas, auditoría e integración." "Java 17 + Spring Boot" {
                # ── Capa de entrada (Controllers) ──
                prestamoCtrl  = component "PrestamoController"  "Endpoints REST de préstamos. Usa SolicitudPrestamoBuilder." "Spring REST"
                usuarioCtrl   = component "UsuarioController"   "Endpoints REST de usuarios. Usa FabricaDeUsuarios."         "Spring REST"

                # ── Capa de Use Cases ──
                registrarPrestamoUC = component "RegistrarPrestamoUseCase" "Orquesta el flujo de un préstamo nuevo."   "Java"
                autenticarUC        = component "AutenticarUsuarioUseCase" "Valida credenciales contra el directorio." "Java"

                # ── Patrones (Sección 2) ──
                fabricaUsuarios   = component "FabricaDeUsuarios"        "Factory: crea Estudiante, Bibliotecario o Admin." "Factory"
                builderSolicitud  = component "SolicitudPrestamoBuilder" "Builder: construye SolicitudPrestamo inmutable."  "Builder"
                auditLogger       = component "AuditoriaLogger"          "Singleton: única instancia, registra todo evento." "Singleton"
                catalogoAdapter   = component "CatalogoCETYSAdapter"     "Adapter: traduce CatalogoBiblioteca ↔ Catálogo CETYS SOAP." "Adapter"

                # ── Puertos (interfaces) y Gateways ──
                repoPrestamos    = component "RepositorioPrestamos"  "Interfaz (puerto) para persistir préstamos." "Interface"
                repoPrestamosImp = component "RepoPrestamosJPA"      "Implementación JPA del repositorio."         "Spring Data JPA"
                pagosGateway     = component "PagosGateway"          "Cliente REST del Sistema de Pagos."          "Spring WebClient"
                directorioGw     = component "DirectorioLDAPGateway" "Cliente LDAP del directorio institucional."  "Spring LDAP"

                # ── Relaciones internas ──
                prestamoCtrl -> builderSolicitud    "Construye SolicitudPrestamo"
                prestamoCtrl -> registrarPrestamoUC "Invoca caso de uso"
                usuarioCtrl  -> fabricaUsuarios     "Solicita instancia de Usuario"
                usuarioCtrl  -> autenticarUC        "Invoca autenticación"

                registrarPrestamoUC -> repoPrestamos  "Persiste préstamo (vía interfaz)"
                registrarPrestamoUC -> catalogoAdapter "Verifica disponibilidad del libro"
                registrarPrestamoUC -> pagosGateway    "Cobra fianza"
                registrarPrestamoUC -> auditLogger     "registrar(evento, usuario)"

                autenticarUC -> auditLogger  "registrar(evento, usuario)"
                autenticarUC -> directorioGw "Valida credenciales"

                repoPrestamos -> repoPrestamosImp "Implementado por"
            }
            worker = container "Worker de Notificaciones" "Envía correos y recordatorios de devolución." "Python + Celery"
            cola   = container "Cola de Mensajes" "Desacopla API y Worker; eventos asíncronos." "RabbitMQ" "Queue"
            db     = container "Base de Datos Operativa" "Préstamos, reservas, multas, usuarios." "PostgreSQL 16" "Database"
            audit  = container "Base de Datos de Auditoría" "Log centralizado e inmutable de toda acción." "PostgreSQL append-only" "Database"
        }

        # ── Sistemas externos ──
        catalogo   = softwareSystem "Catálogo CETYS"                       "Catálogo institucional de libros."           "External"
        pagos      = softwareSystem "Sistema de Pagos Bancario"            "Procesa cobros de fianzas y multas."         "External"
        directorio = softwareSystem "Directorio Estudiantil Institucional" "Autenticación y datos de alumnos."           "External"

        # ── Relaciones de Contexto ──
        estudiante    -> biblioteca "Solicita préstamos, reserva salas, consulta multas"
        bibliotecario -> biblioteca "Aprueba préstamos, registra devoluciones, gestiona multas"
        admin         -> biblioteca "Administra usuarios y políticas"
        biblioteca    -> catalogo   "Consulta disponibilidad y datos de libros" "SOAP"
        biblioteca    -> pagos      "Cobra fianzas y multas"                    "HTTPS/REST"
        biblioteca    -> directorio "Autentica usuarios y obtiene datos"        "LDAP"

        # ── Relaciones de Contenedores ──
        estudiante    -> webapp "Usa" "HTTPS"
        bibliotecario -> webapp "Usa" "HTTPS"
        admin         -> webapp "Usa" "HTTPS"

        webapp -> api "Llama operaciones de negocio" "JSON/HTTPS"

        api -> db    "Lee y escribe datos operativos" "JDBC"
        api -> audit "Registra eventos de auditoría"  "JDBC (append-only)"
        api -> cola  "Publica eventos de notificación" "AMQP"

        cola   -> worker "Entrega mensajes" "AMQP"
        worker -> audit  "Registra envíos de notificación" "JDBC (append-only)"

        api -> catalogo   "Consulta libros"          "SOAP/HTTPS"
        api -> pagos      "Cobra fianzas y multas"   "HTTPS/REST"
        api -> directorio "Autentica y consulta alumnos" "LDAP"
    }

    views {
        systemContext biblioteca "Contexto" {
            include *
            autoLayout
        }

        container biblioteca "Contenedores" {
            include *
            autoLayout
        }

        component api "Componentes_API" {
            include *
            autoLayout
        }

        styles {
            element "Person" {
                shape Person
                background "#08427b"
                color "#ffffff"
            }
            element "Software System" {
                background "#1168bd"
                color "#ffffff"
            }
            element "Container" {
                background "#438dd5"
                color "#ffffff"
            }
            element "WebApp" {
                shape WebBrowser
            }
            element "Database" {
                shape Cylinder
            }
            element "Queue" {
                shape Pipe
            }
            element "External" {
                background "#999999"
                color "#ffffff"
            }
            element "Interface" {
                shape Component
                background "#85bbf0"
                color "#000000"
            }
            element "Singleton" {
                background "#cc6600"
                color "#ffffff"
            }
            element "Factory" {
                background "#7b1fa2"
                color "#ffffff"
            }
            element "Builder"{
                background "#2e7d32"
                color "#ffffff"
            }
            element "Adapter" {
                background "#c62828"
                color "#ffffff"
            }
        }
    }
}