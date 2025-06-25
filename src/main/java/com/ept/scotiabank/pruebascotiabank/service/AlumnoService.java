package com.ept.scotiabank.pruebascotiabank.service;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlumnoService {
    Mono<Void> guardarAlumno(Alumno alumno);
    Flux<Alumno> obtenerAlumnosActivos();
}
