package es.biblioteca.userservice.domain.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {

    /**
     * Coincide con todos los métodos de cualquier clase dentro del paquete 'service'
     * y TODOS sus sub-paquetes. Es la forma más robusta.
     */
    @Pointcut("within(es.biblioteca.userservice.application.service..*)")
    public void pointcutServicePackage() {}

    /**
     * Coincide con todos los métodos de cualquier clase dentro del paquete 'web'
     * (donde viven tus controladores) y TODOS sus sub-paquetes.
     * Esto interceptará AuthController, UserController y cualquier otro que añadas.
     */
    @Pointcut("within(es.biblioteca.userservice.infrastructure.adapter.in.web..*)")
    public void pointcutControllerPackage() {} // Renombrado para mayor claridad

    /**
     * Coincide con todos los métodos de cualquier clase dentro del paquete 'persistence'
     * y TODOS sus sub-paquetes.
     */
    @Pointcut("within(es.biblioteca.userservice.infrastructure.adapter.out.persistence..*)")
    public void pointcutPersistencePackage() {}

    /**
     * Coincide con todos los métodos de cualquier clase dentro del paquete 'factory'
     * y TODOS sus sub-paquetes.
     */
    @Pointcut("within(es.biblioteca.userservice.infrastructure.adapter.out.factory..*)")
    public void pointcutFactoryPackage() {}

    /**
     * Para JwtProvider, mantenemos 'execution' porque es más preciso y fiable
     * al tratar con proxies de Spring que implementan interfaces.
     * Esto ya sabemos que funciona y es la herramienta correcta para este caso específico.
     */
    @Pointcut("execution(* es.biblioteca.userservice.infrastructure.adapter.out.security.JwtProvider.*(..))")
    public void pointcutJwtProvider() {}
}
