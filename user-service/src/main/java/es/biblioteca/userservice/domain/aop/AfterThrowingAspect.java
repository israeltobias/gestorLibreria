package es.biblioteca.userservice.domain.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AfterThrowingAspect {

    private static final Logger log = LoggerFactory.getLogger(AfterThrowingAspect.class);
    private final SecureData secureData;

    @AfterThrowing(pointcut = "PointCuts.pointcutServicePackage() " +
            "|| PointCuts.pointcutControllerPackage() " +
            "|| PointCuts.pointcutPersistencePackage() " +
            "|| PointCuts.pointcutFactoryPackage() " +
            "|| PointCuts.pointcutJwtProvider()"
            , throwing = "error")
    public void afterThrowingAspect(JoinPoint joinPoint, Throwable error) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String methodArgs = Arrays.toString(joinPoint.getArgs());

        // --- Construir un mensaje de log detallado y estructurado ---
        log.error(
                "\n<!!!> EXCEPCIÓN LANZADA <!!!>\n" +
                        "CLASE      : {}\n" +
                        "MÉTODO     : {}\n" +
                        "ARGUMENTOS : {}\n" +
                        "EXCEPCIÓN  : {}\n" +
                        "MENSAJE    : {}\n" +
                        "TRAZA      : ",
                className,
                methodName,
                methodArgs, // ¡CUIDADO! Aplica el enmascaramiento si es necesario
                error.getClass().getName(),
                error.getMessage(),
                error
        );
    }
}
