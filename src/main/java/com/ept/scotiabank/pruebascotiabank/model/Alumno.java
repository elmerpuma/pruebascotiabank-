package com.ept.scotiabank.pruebascotiabank.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("alumnos")
public class Alumno {
    @Id
    @Column("id")
    private String id; // Puede ser UUID generado desde el backend

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Pattern(regexp = "activo|inactivo", message = "Estado debe ser 'activo' o 'inactivo'")
    private String estado;

    @Min(value = 1, message = "Edad mínima es 1")
    @Max(value = 120, message = "Edad máxima es 120")
    private int edad;

}


