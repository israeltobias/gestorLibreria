package es.biblioteca;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceApplicationTest {

  
    @Test
    @DisplayName("La aplicación debería cargar el contexto y la clase principal")
    void applicationContextShouldLoad(ApplicationContext context) {
        assertThat(context).isNotNull();
        assertThat(context.getBean(UserServiceApplication.class)).isNotNull();
    }
}