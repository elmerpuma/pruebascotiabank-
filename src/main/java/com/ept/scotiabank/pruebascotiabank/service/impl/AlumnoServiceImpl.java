package com.ept.scotiabank.pruebascotiabank.service.impl;

import com.ept.scotiabank.pruebascotiabank.model.Alumno;
import com.ept.scotiabank.pruebascotiabank.repository.AlumnoRepository;
import com.ept.scotiabank.pruebascotiabank.service.AlumnoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired  // Inyección de dependencia del repositorio
    private AlumnoRepository repo;
    private static final Logger log = LoggerFactory.getLogger(AlumnoServiceImpl.class);
    @Autowired
    private DatabaseClient databaseClient;

    public AlumnoServiceImpl(AlumnoRepository repo) {
        this.repo = repo;
    }
    @Override
    public Mono<Void> guardarAlumno(Alumno alumno) {
        log.info("Intentando guardar alumno con ID: {}", alumno.getId());

        return repo.existsById(alumno.getId())
                .flatMap(existe -> {
                    if (existe) {
                        log.warn("El alumno con ID {} ya existe. No se puede guardar.", alumno.getId());
                        return Mono.error(new IllegalArgumentException("ID duplicado"));
                    } else {
                        log.info("Insertando alumno {} con INSERT directo", alumno.getId());

                        return databaseClient.sql("""
                    INSERT INTO alumnos (id, nombre, apellido, estado, edad)
                    VALUES (:id, :nombre, :apellido, :estado, :edad)
                """)
                                .bind("id", alumno.getId())
                                .bind("nombre", alumno.getNombre())
                                .bind("apellido", alumno.getApellido())
                                .bind("estado", alumno.getEstado())
                                .bind("edad", alumno.getEdad())
                                .then()
                                .doOnSuccess(v -> log.info("Alumno {} guardado correctamente", alumno.getId()));
                    }
                });
    }


    @Override
    public Flux<Alumno> obtenerAlumnosActivos() {
        return repo.findByEstado("activo");
    }
}

