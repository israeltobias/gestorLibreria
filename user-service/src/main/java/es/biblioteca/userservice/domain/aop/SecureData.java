package es.biblioteca.userservice.domain.aop;

import es.biblioteca.userservice.domain.annotations.LogMasked;
import es.biblioteca.userservice.infrastructure.adapter.out.persistence.UserRepositoryAdapter;
import es.biblioteca.userservice.infrastructure.adapter.out.security.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SecureData {

    private static final String MASK = "'[*****]'";
    private static final String APP_BASE_PACKAGE = "es.biblioteca.userservice";

    public String checkResultInstances(Object result) {
        switch (result) {
            case null -> {
                return "[void]";
            }
            case Optional<?> opt -> {
                return opt.map(o -> "Optional[" + getSecureStringForObject(o) + "]").orElseGet(() ->"Optional.empty");
            }
            case ResponseEntity<?> responseEntity -> {
                Object body = responseEntity.getBody();
                String secureBody = getSecureStringForObject(body);

                return "<" + responseEntity.getStatusCode() + "," + secureBody + "," + responseEntity.getHeaders() + ">";
            }
            default -> {
                return getSecureStringForObject(result);
            }
        }
    }

    /**
     * Convierte un array de argumentos a un String, enmascarando los campos necesarios.
     */
    public String getSecureArgsString(Annotation[][] parameterAnnotations, Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        return IntStream.range(0, args.length)
                .mapToObj(i -> {
                    // Primero, comprobamos si el PARÁMETRO en la posición 'i' está anotado
                    for (Annotation annotation : parameterAnnotations[i]) {
                        if (annotation instanceof LogMasked) {
                            return MASK; // Si lo está, enmascaramos y terminamos
                        }
                    }
                    // Si el parámetro no estaba anotado, pasamos el OBJETO a la lógica de inspección de campos
                    return getSecureStringForObject(args[i]);
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }
    /**
     * Convierte un objeto individual a un String, inspeccionando sus campos para enmascarar.
     */
    public String getSecureStringForObject(Object arg) {
        if (arg == null) {
            return "null";
        }
        // Si el objeto no pertenece a nuestra aplicación, confiamos en su toString()
        // y no intentamos hacer reflexión profunda.
        if (!isAppClass(arg.getClass())) {
            return arg.toString();
        }

        if (arg instanceof String || arg instanceof Number || arg.getClass().isPrimitive() || arg instanceof Enum) {
            return arg.toString();
        }

        if (arg instanceof Collection<?> collection) {
            return collection.stream()
                    .map(this::getSecureStringForObject)
                    .collect(Collectors.joining(", ", "[", "]"));
        }

        StringBuilder sb = new StringBuilder(arg.getClass().getSimpleName()).append("[");
        Field[] fields = arg.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            sb.append(field.getName()).append("=");
            try {
                if (field.isAnnotationPresent(LogMasked.class)) {
                    sb.append(MASK);
                } else {
                    Object value = field.get(arg);
                    sb.append(value != null ? getSecureStringForObject(value) : "null");
                }
            } catch (IllegalAccessException _) {
                sb.append("[ACCESO DENEGADO]");
            }

            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    // 3. MÉTODO HELPER PARA COMPROBAR SI LA CLASE ES DE NUESTRA APLICACIÓN
    private boolean isAppClass(Class<?> clazz) {
        return clazz.getPackage() != null && clazz.getPackage().getName().startsWith(APP_BASE_PACKAGE);
    }
}
