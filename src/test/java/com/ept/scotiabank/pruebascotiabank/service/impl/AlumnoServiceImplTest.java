package com.ept.scotiabank.pruebascotiabank.service.impl;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import com.ept.scotiabank.pruebascotiabank.repository.AlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class AlumnoServiceImplTest {

    private AlumnoRepository repo;
    private AlumnoServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(AlumnoRepository.class);
        service = new AlumnoServiceImpl(repo); // Constructor inyectado
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

    @Test
    void guardarAlumno_idNuevo_debeGuardarCorrectamente() {
        Alumno alumno = new Alumno("a005", "Ana", "Lopez", "activo", 22);

        when(repo.existsById("a005")).thenReturn(Mono.just(false));
        when(repo.save(alumno)).thenReturn(Mono.just(alumno));

        StepVerifier.create(service.guardarAlumno(alumno))
                .verifyComplete();

        verify(repo).save(alumno);
    }
}

