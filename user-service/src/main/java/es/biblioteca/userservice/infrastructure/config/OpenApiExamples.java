package es.biblioteca.userservice.infrastructure.config;

public class OpenApiExamples {



    private OpenApiExamples() {
        super();
    }
    // --- EJEMPLOS DE PETICIÓN ---1
    public static final String VALID_OBJECT_REGISTER = """
            {
              "username": "roberto",
              "password": "robertpass",
              "roles": [
                "USER","ADMIN"
              ]
            }
            """;

    public static final String VALID_OBJECT_LOGIN = """
            {
              "username": "juan",
              "password": "juanpass"
              }
            """;
    // --- EJEMPLOS DE RESPUESTA ---

    public static final String RESPONSE_201_USER_CREATED = """
            {
              "status": 201,
              "message": "Usuario creado correctamente.",
              "path": "/auth/register",
              "time": "2025-11-28T14:30:00.123Z",
              "data": {
                "username": "roberto",
                "roles": ["USER", "ADMIN"]
              },
              "pagination": null
            }
            """;

    public static final String RESPONSE_200_LOGIN_SUCCESS = """
            {
              "status": 200,
              "message": "Token generado correctamente",
              "path": "/auth/login",
              "time": "2025-11-28T14:30:00.123Z",
              "data": {
                "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic..."
              },
              "pagination": null
            }
            """;

    public static final String RESPONSE_404_NOT_FOUND = """
            {
              "code": "DATA_NOT_FOUND",
              "message": "No se han encontrado datos",
              "timestamp": "2025-11-28T14:30:00.123Z",
              "path": "/PATH"
            }
            """;
    public static final String RESPONSE_409_CONFLICT = """
            {
              "code": "CONFLICT",
              "message": "El usuario ya exsite",
              "timestamp": "2025-11-28T14:30:00.123Z",
              "path": "7PATH"
            }
            """;
    public static final String RESPONSE_400_BAD_DATA = """
            {
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Invalid request content.",
                "instance": "/path"
            }
            """;

    public static final String RESPONSE_403_NOT_ADMINISTRATOR = """
            {
                "code": "AUTHENTICATION_FAILED",
                "message": "No está autorizado para acceder a este recurso",
                "timestamp": "2025-12-01T08:40:13.479749729Z",
                "path": "/PATH"
            }
            """;

    public static final String RESPONSE_200_USERS = """
                {
                    "status": 200,
                    "message": "Datos obtenidos correctamente",
                    "path": "/user",
                    "time": "2025-12-01T09:28:36.798272509Z",
                    "data": [
                        {
                            "username": "juan",
                            "roles": [
                                "USER"
                            ]
                        },
                        {
                            "username": "maria",
                            "roles": [
                                "USER",
                                "ADMIN"
                            ]
                        }
                    ],
                    "pagination": null
                }
            """;

    public static final String RESPONSE_401_UNAUTHORIZED = """
            {
                "code": "AUTHENTICATION_FAILED",
                "message": "No está autorizado para acceder a este recurso",
                "timestamp": "2025-12-01T09:25:31.590608623Z",
                "path": "/PATH"
            }
            """;

    public static final String RESPONSE_200_USER = """
            {
                "status": 200,
                "message": "Usuario obtenido correctamente",
                "path": "/PATH",
                "time": "2025-12-01T10:28:00.145255971Z",
                "data": {
                    "username": "usuario",
                    "roles": [
                        "role"
                    ]
                },
                "pagination": null
            }
            """;
}
