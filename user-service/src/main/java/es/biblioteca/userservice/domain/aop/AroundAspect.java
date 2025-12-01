package es.biblioteca.userservice.domain.aop;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AroundAspect {

    private static final Logger log = LoggerFactory.getLogger(AroundAspect.class);
    private final SecureData secureData;

    @Around("PointCuts.pointcutServicePackage() " +
            "|| PointCuts.pointcutControllerPackage() " +
            "|| PointCuts.pointcutPersistencePackage() " +
            "|| PointCuts.pointcutFactoryPackage() " +
            "|| PointCuts.pointcutJwtProvider()")
    public Object aroundAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String argsString = secureData.getSecureArgsString(method.getParameterAnnotations(), args);

        log.info("===> [INICIO] {}.{}() | ARGS: {}", className, methodName, argsString);
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        String resultString = secureData.checkResultInstances(result);
        log.info("<=== [FIN] {}.{}() | RETORNO: {} | TIEMPO: {}ms", className, methodName, resultString, executionTime);
        return result;

    }



}
