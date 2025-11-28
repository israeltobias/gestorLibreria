# Gestor de Librería (Library Management System)

Bienvenido al Gestor de Librería, un sistema de microservicios diseñado para administrar las operaciones de una biblioteca moderna. Este proyecto está construido siguiendo los principios de la **Arquitectura Hexagonal** para asegurar un código limpio, desacoplado, mantenible y fácilmente testeable.

## Visión General del Proyecto

El sistema se compone (o se compondrá) de varios módulos independientes que se comunican entre sí para ofrecer una funcionalidad completa.

### Módulos

-   **`user-service` (¡Implementado!)**: Gestiona toda la lógica relacionada con usuarios, incluyendo autenticación, autorización, registro y gestión de perfiles.
-   **`book-service` (Futuro)**: Se encargará del catálogo de libros, autores, géneros, inventario y disponibilidad.
-   **`gateway-service` (Futuro)**: Actuará como un API Gateway, siendo el único punto de entrada para los clientes. Gestionará el enrutamiento de peticiones, la seguridad perimetral y el balanceo de carga.

## Arquitectura y Diseño

El proyecto está fuertemente influenciado por la **Arquitectura Hexagonal (Puertos y Adaptadores)**. Esto significa que la lógica de negocio (el "dominio") está completamente aislada de las tecnologías externas (frameworks, bases de datos, etc.).

-   **Dominio (`domain`)**: El núcleo de la aplicación. Contiene los modelos de negocio puros (`User`, `Role`), las reglas de negocio y las interfaces de los puertos. No tiene dependencias de Spring ni de ninguna tecnología de infraestructura.
-   **Aplicación (`application`)**: La capa de orquestación. Contiene los casos de uso (`UserUseCase`), implementa la lógica de la aplicación y conecta los puertos de entrada con los de salida.
-   **Infraestructura (`infrastructure`)**: Los detalles de implementación. Contiene los **adaptadores** que se conectan a los puertos del dominio. Aquí viven los controladores REST, los repositorios JPA, los proveedores de JWT, la configuración de Spring, etc.

## Stack Tecnológico Principal

-   **Lenguaje**: **Java 25**
-   **Framework**: **Spring Boot 3.x**
-   **Seguridad**: **Spring Security 6.x**
    -   Autenticación basada en **JWT (JSON Web Tokens)**.
    -   Soporte para **Refresh Tokens** para una gestión de sesiones segura y duradera.
-   **Base de Datos**: **Spring Data JPA** con **Hibernate**.
    -   **H2 Database** para desarrollo y pruebas (en memoria).
-   **API**: RESTful
-   **Construcción**: **Maven**
-   **Observabilidad**:
    -   Logging centralizado con **SLF4J** y **Logback**.
    -   Monitorización de ejecución y excepciones mediante **Spring AOP (Aspect-Oriented Programming)**.

## Características del `user-service`

### Seguridad
-   **Autenticación sin estado (Stateless)** usando JWTs.
-   **Autorización basada en roles**.
-   Endpoints protegidos con reglas granulares.
-   **Filtros de seguridad personalizados** para:
    -   Validación de tokens JWT en cada petición (`JwtAuthenticationFilter`).
    -   Protección a nivel de servidor contra peticiones de orígenes no autorizados (`OriginValidationFilter`).
-   Configuración **CORS** robusta y externalizada para permitir la comunicación con front-ends.
-   Manejo de errores de seguridad centralizado (`AuthenticationEntryPoint`, `AccessDeniedHandler`) para respuestas de API consistentes.
-   Protección de contraseñas mediante **BCrypt**.

### API Endpoints
-   `POST /auth/login`: Autenticación de usuarios y generación de `accessToken` y `refreshToken`.
-   `POST /auth/register`: Registro de nuevos usuarios (restringido a `ROLE_ADMIN`).
-   `GET /users`: Obtención de una lista de usuarios (endpoint de ejemplo protegido).
-   `GET /user/username`: Obtención de un usuario usando su nombre de usuario.

### Logging y Monitorización
-   Se utiliza AOP para registrar automáticamente:
    -   Inicio y fin de la ejecución de métodos en las capas de controlador, servicio y persistencia.
    -   Argumentos de entrada (con **enmascaramiento de datos sensibles** como contraseñas).
    -   Valores de retorno (con enmascaramiento de tokens).
    -   Tiempo de ejecución de cada método.
    -   Excepciones no controladas, incluyendo su traza de pila completa.

## Cómo Empezar

### Prerrequisitos
-   JDK 25 o superior.
-   Apache Maven 3.8+.
-   Una IDE como IntelliJ IDEA o VS Code.

### Ejecución Local
1.  Clona el repositorio:
    ```bash
    git clone https://tu-repositorio.com/gestor-libreria.git
    ```
2.  Navega al directorio del módulo `user-service`:
    ```bash
    cd gestor-libreria/user-service
    ```
3.  Construye el proyecto con Maven:
    ```bash
    mvn clean install
    ```
4.  Ejecuta la aplicación:
    ```bash
    mvn spring-boot:run
    ```
    La aplicación se iniciará en `http://localhost:8081`.
