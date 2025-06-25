package com.ept.scotiabank.pruebascotiabank.controller;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import com.ept.scotiabank.pruebascotiabank.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService service;

    @PostMapping
    public Mono<ResponseEntity<Object>> guardar(@Valid @RequestBody Mono<Alumno> alumnoMono) {
        return alumnoMono
                .flatMap(alumno ->
                        service.guardarAlumno(alumno)
                                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()))
                )
                .onErrorResume(WebExchangeBindException.class, e -> {
                    // Validación de campos fallida
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(IllegalArgumentException.class, e -> {
                    // ID duplicado u otro error de negocio controlado
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(Exception.class, e -> {
                    // Errores inesperados
                    System.err.println("Error inesperado: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @GetMapping("/activos")
    public Flux<Alumno> listarActivos() {
        return service.obtenerAlumnosActivos();
    }
}

