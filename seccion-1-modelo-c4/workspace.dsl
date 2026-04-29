workspace "Biblioteca CETYS" "Sistema de gestion de biblioteca universitaria con integracion a sistemas externos." {

    model {
        # ============================================================
        # ACTORES HUMANOS (nivel Contexto)
        # ============================================================
        estudiante    = person "Estudiante"      "Solicita prestamos, reserva salas y paga multas."
        bibliotecario = person "Bibliotecario"   "Gestiona prestamos, devoluciones y multas."
        admin         = person "Administrador"   "Administra usuarios, politicas y configuracion."

        # ============================================================
        # SISTEMA BAJO DISENO (con sus contenedores y componentes)
        # ============================================================
        biblioteca = softwareSystem "Sistema de Biblioteca CETYS" "Gestiona prestamos de libros, reservas de salas y multas a estudiantes." {

            # ---- CONTENEDORES (nivel 2) ----
            webapp = container "Web App" "SPA para estudiantes, bibliotecarios y administradores." "React + TypeScript" "WebApp"

            api = container "API Backend" "Logica de negocio: prestamos, reservas, multas, autenticacion e integracion con sistemas externos." "Java 17 + Spring Boot" {

                # ---- COMPONENTES (nivel 3) del API Backend ----

                # Capa de entrada (Controllers)
                prestamoCtrl = component "PrestamoController" "Endpoints REST de prestamos. Usa SolicitudPrestamoBuilder." "Spring REST"
                usuarioCtrl  = component "UsuarioController"  "Endpoints REST de usuarios. Usa FabricaDeUsuarios." "Spring REST"

                # Capa de Use Cases (aplicacion)
                registrarPrestamoUC = component "RegistrarPrestamoUseCase" "Orquesta el flujo de un prestamo nuevo." "Java"
                autenticarUC        = component "AutenticarUsuarioUseCase" "Valida credenciales contra el directorio." "Java"

                # Patrones de diseno (Seccion 2)
                fabricaUsuarios  = component "FabricaDeUsuarios"        "Factory: crea Estudiante, Bibliotecario o Admin sin exponer clases concretas." "Factory"
                builderSolicitud = component "SolicitudPrestamoBuilder" "Builder: construye SolicitudPrestamo inmutable y validada." "Builder"
                auditLogger      = component "AuditoriaLogger"          "Singleton: unica instancia, registra todo evento del sistema." "Singleton"
                catalogoAdapter  = component "CatalogoCETYSAdapter"     "Adapter: traduce CatalogoBiblioteca <-> Catalogo CETYS SOAP." "Adapter"

                # Puertos (interfaces) y Gateways
                repoPrestamos    = component "RepositorioPrestamos"    "Puerto (interfaz) para persistir prestamos." "Interface"
                repoPrestamosImp = component "RepoPrestamosJPA"        "Implementacion JPA del repositorio." "Spring Data JPA"
                pagosGateway     = component "PagosGateway"            "Cliente REST del Sistema de Pagos." "Spring WebClient"
                directorioGw     = component "DirectorioLDAPGateway"   "Cliente LDAP del directorio institucional." "Spring LDAP"

                # Relaciones internas del API
                prestamoCtrl -> builderSolicitud    "Construye SolicitudPrestamo"
                prestamoCtrl -> registrarPrestamoUC "Invoca caso de uso"
                usuarioCtrl  -> fabricaUsuarios     "Solicita instancia de Usuario"
                usuarioCtrl  -> autenticarUC        "Invoca autenticacion"

                registrarPrestamoUC -> repoPrestamos   "Persiste prestamo (via interfaz)"
                registrarPrestamoUC -> catalogoAdapter "Verifica disponibilidad del libro"
                registrarPrestamoUC -> pagosGateway    "Cobra fianza"
                registrarPrestamoUC -> auditLogger     "registrar(evento, usuario)"

                autenticarUC -> directorioGw "Valida credenciales"
                autenticarUC -> auditLogger  "registrar(evento, usuario)"

                repoPrestamos -> repoPrestamosImp "Implementado por"
            }

            worker = container "Worker de Notificaciones" "Procesa mensajes de la cola y envia correos/recordatorios sin bloquear la API." "Python + Celery"
            cola   = container "Cola de Mensajes" "Desacopla API y Worker; eventos asincronos." "RabbitMQ" "Queue"
            db     = container "Base de Datos Operativa" "Prestamos, reservas, multas, usuarios." "PostgreSQL 16" "Database"
            audit  = container "Base de Datos de Auditoria" "Log centralizado e inmutable de toda accion (append-only)." "PostgreSQL append-only" "Database"
        }

        # ============================================================
        # SISTEMAS EXTERNOS (nivel Contexto)
        # ============================================================
        catalogo   = softwareSystem "Catalogo CETYS"                       "Catalogo institucional de libros (SOAP)."     "External"
        pagos      = softwareSystem "Sistema de Pagos Bancario"            "Procesa cobros de fianzas y multas (REST)."   "External"
        directorio = softwareSystem "Directorio Estudiantil Institucional" "Autenticacion y datos oficiales (LDAP)."      "External"

        # ============================================================
        # RELACIONES NIVEL CONTEXTO
        # ============================================================
        estudiante    -> biblioteca "Solicita prestamos, reserva salas, consulta multas"
        bibliotecario -> biblioteca "Aprueba prestamos, registra devoluciones, gestiona multas"
        admin         -> biblioteca "Administra usuarios y politicas"

        biblioteca -> catalogo   "Consulta disponibilidad y datos de libros" "SOAP"
        biblioteca -> pagos      "Cobra fianzas y multas"                    "HTTPS/REST"
        biblioteca -> directorio "Autentica usuarios y obtiene datos"        "LDAP"

        # ============================================================
        # RELACIONES NIVEL CONTENEDORES
        # ============================================================
        estudiante    -> webapp "Usa" "HTTPS"
        bibliotecario -> webapp "Usa" "HTTPS"
        admin         -> webapp "Usa" "HTTPS"

        webapp -> api "Llama operaciones de negocio" "JSON/HTTPS"

        api -> db    "Lee y escribe datos operativos" "JDBC"
        api -> audit "Registra eventos de auditoria"  "JDBC (append-only)"
        api -> cola  "Publica eventos de notificacion" "AMQP"

        cola   -> worker "Entrega mensajes" "AMQP"
        worker -> audit  "Registra envios de notificacion" "JDBC (append-only)"

        api -> catalogo   "Consulta libros"             "SOAP/HTTPS"
        api -> pagos      "Cobra fianzas y multas"      "HTTPS/REST"
        api -> directorio "Autentica y consulta alumnos" "LDAP"
    }

    views {
        systemContext biblioteca "Contexto" {
            include *
            autoLayout
            description "Diagrama C4 nivel 1: Contexto del Sistema de Biblioteca CETYS"
        }

        container biblioteca "Contenedores" {
            include *
            autoLayout
            description "Diagrama C4 nivel 2: Contenedores del Sistema de Biblioteca CETYS"
        }

        component api "Componentes_API" {
            include *
            autoLayout
            description "Diagrama C4 nivel 3: Componentes internos del API Backend"
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
            element "Component" {
                background "#85bbf0"
                color "#000000"
            }
            element "WebApp"   { shape WebBrowser }
            element "Database" { shape Cylinder }
            element "Queue"    { shape Pipe }
            element "External" {
                background "#999999"
                color "#ffffff"
            }
            element "Interface" {
                background "#bdd7ee"
                color "#000000"
            }
            element "Singleton" { background "#cc6600" color "#ffffff" }
            element "Factory"   { background "#7b1fa2" color "#ffffff" }
            element "Builder"   { background "#2e7d32" color "#ffffff" }
            element "Adapter"   { background "#c62828" color "#ffffff" }
        }
    }
}
