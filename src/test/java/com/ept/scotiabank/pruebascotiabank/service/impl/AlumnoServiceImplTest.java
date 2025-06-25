package com.ept.scotiabank.pruebascotiabank.service.impl;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import com.ept.scotiabank.pruebascotiabank.repository.AlumnoRepository;
import com.ept.scotiabank.pruebascotiabank.service.AlumnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlumnoServiceImplTest {

    private AlumnoRepository repo;
    private AlumnoServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(AlumnoRepository.class);
        service = new AlumnoServiceImpl(repo);
    }

    @Test
    void guardarAlumno_idDuplicado_debeRetornarError() {
        Alumno alumno = new Alumno("a001", "Juan", "Pérez", "activo", 20);

        when(repo.existsById("a001")).thenReturn(Mono.just(true));

        StepVerifier.create(service.guardarAlumno(alumno))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(repo, times(1)).existsById("a001");
        verify(repo, never()).save(any());
    }
}
    @DataR2dbcTest
    @Import(AlumnoServiceImpl.class) // Importa tu servicio
    class AlumnoServiceIntegrationTest {

        @Autowired
        private AlumnoService service;

        @Autowired
        private AlumnoRepository repo;

        @Test
        void guardarAlumno_idNuevo_debeGuardarCorrectamente() {
            Alumno alumno = new Alumno("a005", "Ana", "Lopez", "activo", 22);

            StepVerifier.create(service.guardarAlumno(alumno))
                    .verifyComplete();

            // Verificar que realmente se guardó
            StepVerifier.create(repo.findById("a005"))
                    .assertNext(a -> {
                        assertEquals("a005", a.getId());
                        assertEquals("Ana", a.getNombre());
                        assertEquals("Lopez", a.getApellido());
                        assertEquals("activo", a.getEstado());
                        assertEquals(22, a.getEdad());
                    })
                    .verifyComplete();
        }

        @Autowired
        private DatabaseClient databaseClient;

        @BeforeEach
        void setUp() {
            databaseClient.sql("DELETE FROM alumnos").fetch().rowsUpdated().block();

            Flux.just(
                    new Alumno("a101", "Mario", "Quispe", "activo", 21),
                    new Alumno("a102", "Lucía", "Ramos", "inactivo", 23),
                    new Alumno("a103", "Carlos", "Vera", "activo", 20)
            ).flatMap(alumno -> databaseClient.sql("""
            INSERT INTO alumnos (id, nombre, apellido, estado, edad)
            VALUES (:id, :nombre, :apellido, :estado, :edad)
        """)
                    .bind("id", alumno.getId())
                    .bind("nombre", alumno.getNombre())
                    .bind("apellido", alumno.getApellido())
                    .bind("estado", alumno.getEstado())
                    .bind("edad", alumno.getEdad())
                    .then()
            ).blockLast();
        }
        @Test
        void listarAlumnosActivos_debeRetornarSoloActivos() {
            StepVerifier.create(service.obtenerAlumnosActivos())
                    .recordWith(ArrayList::new)
                    .expectNextCount(2)
                    .consumeRecordedWith(alumnos -> {
                        assertEquals(2, alumnos.size());
                        alumnos.forEach(a -> assertEquals("activo", a.getEstado()));
                    })
                    .verifyComplete();
        }
    }





