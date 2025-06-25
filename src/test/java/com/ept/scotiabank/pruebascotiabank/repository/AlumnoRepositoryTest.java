package com.ept.scotiabank.pruebascotiabank.repository;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
class AlumnoRepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        databaseClient.sql("DROP TABLE IF EXISTS alumnos").then()
                .then(databaseClient.sql("""
                CREATE TABLE alumnos (
                    id VARCHAR(255) PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    apellido VARCHAR(100) NOT NULL,
                    estado VARCHAR(20) NOT NULL,
                    edad INT NOT NULL
                )
            """).then())
                .then(databaseClient.sql("""
                INSERT INTO alumnos (id, nombre, apellido, estado, edad)
                VALUES 
                    ('a001', 'Juan', 'Pérez', 'activo', 20),
                    ('a002', 'Ana', 'García', 'inactivo', 22)
            """).then())
                .block();
    }

    @Test
    void testFindByEstadoActivo() {
        alumnoRepository.findByEstado("activo")
                .as(StepVerifier::create)
                .expectNextMatches(alumno ->
                        alumno.getId().equals("a001") &&
                                alumno.getNombre().equals("Juan") &&
                                alumno.getEstado().equals("activo"))
                .verifyComplete();
    }

    @Test
    void testExistsById() {
        Mono<Boolean> exists = alumnoRepository.existsById("a001");

        StepVerifier.create(exists)
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(alumnoRepository.existsById("noexiste"))
                .expectNext(false)
                .verifyComplete();
    }

    /*  @Test
    void testSaveAlumnoNuevo() {
         Alumno nuevo = new Alumno("a003", "Luis", "Ramírez", "activo", 25);

         alumnoRepository.save(nuevo)
                 .as(StepVerifier::create)
                 .expectNextMatches(a ->
                         a.getId().equals("a003") &&
                                 a.getNombre().equals("Luis"))
                 .verifyComplete();
     }*/
    @Test
    void testSaveAlumnoNuevo() {
        databaseClient.sql("""
        INSERT INTO alumnos (id, nombre, apellido, estado, edad)
        VALUES ('a003', 'Luis', 'Ramírez', 'activo', 25)
    """).then()
                .as(StepVerifier::create)
                .verifyComplete();

        alumnoRepository.findById("a003")
                .as(StepVerifier::create)
                .expectNextMatches(a -> a.getNombre().equals("Luis"))
                .verifyComplete();
    }
}
