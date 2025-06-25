package com.ept.scotiabank.pruebascotiabank.controller;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import com.ept.scotiabank.pruebascotiabank.service.AlumnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = AlumnoController.class)
@Import(AlumnoControllerTest.Config.class) // Importamos la configuración personalizada
class AlumnoControllerTest {

    private static AlumnoService alumnoService = mock(AlumnoService.class);

    @TestConfiguration
    static class Config {
        @Bean
        public AlumnoService alumnoService() {
            return alumnoService;
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(alumnoService);
    }

    @Test
    void guardarAlumno_CuandoEsValido_debeRetornar201() {
        Alumno alumno = new Alumno("a007", "Lucía", "Mendoza", "activo", 25);

        when(alumnoService.guardarAlumno(alumno)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/alumnos")
                .bodyValue(alumno)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void guardarAlumno_CuandoIdDuplicado_debeRetornar400() {
        Alumno alumno = new Alumno("a001", "Juan", "Pérez", "activo", 21);

        when(alumnoService.guardarAlumno(alumno)).thenReturn(Mono.error(new IllegalArgumentException("ID duplicado")));

        webTestClient.post()
                .uri("/alumnos")
                .bodyValue(alumno)
                .exchange()
                .expectStatus().isBadRequest();
    }
}


