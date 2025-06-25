package com.ept.scotiabank.pruebascotiabank.repository;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AlumnoRepository extends ReactiveCrudRepository<Alumno, String> {
    Flux<Alumno> findByEstado(String estado);


}

