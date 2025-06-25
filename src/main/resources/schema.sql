CREATE TABLE alumnos (
    id VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    edad INT NOT NULL
);

-- Datos de ejemplo opcionales
INSERT INTO alumnos (id, nombre, apellido, estado, edad) VALUES
('a001', 'Juan', 'Pérez', 'activo', 21),
('a002', 'Ana', 'Lopez', 'inactivo', 25);
