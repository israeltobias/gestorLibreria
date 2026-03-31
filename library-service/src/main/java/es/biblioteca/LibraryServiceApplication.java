package es.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"es.biblioteca.libraryservice"})
public class LibraryServiceApplication {

    private LibraryServiceApplication() {
        super();
    }

    static void main(String[] args) {
        SpringApplication.run(LibraryServiceApplication.class, args);
    }
}
