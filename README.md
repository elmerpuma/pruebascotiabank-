# 🧪 Prueba Técnica - Microservicios Reactivos con Spring Boot

Este proyecto es una solución para el reto técnico de **Scotiabank Perú**, que evalúa la capacidad de desarrollar microservicios reactivos usando **Java**, **Spring Boot** y **R2DBC**.

---

## 🚀 Tecnologías Usadas

- ✅ Java 17
- ✅ Spring Boot 3.5.3
- ✅ Spring WebFlux
- ✅ Spring Data R2DBC
- ✅ H2 Database (en memoria)
- ✅ Maven
- ✅ Reactor Core y Test
- ✅ JUnit 5 + StepVerifier

---

## 🎯 Requisitos del Reto

### ✅ POST `/alumnos`

Crea un alumno con los campos:

- `id`: único por alumno
- `nombre`: obligatorio
- `apellido`: obligatorio
- `estado`: "activo" o "inactivo"
- `edad`: entre 1 y 120

📌 Validaciones:
- Todos los campos deben validarse.
- El `id` no debe repetirse.

📌 Respuestas:
- `201 Created` si se guarda.
- `400 Bad Request` si falla una validación o el ID ya existe.

---

### ✅ GET `/alumnos/activos`

Retorna todos los alumnos con estado `"activo"`.

---

## 🧠 Arquitectura

Se implementó una arquitectura **por capas**:

```
com.ept.scotiabank.pruebascotiabank
│
├── controller        → Manejo de endpoints (WebFlux)
├── service           → Lógica de negocio
├── repository        → Acceso a datos (Spring R2DBC)
├── model             → Entidad `Alumno`
└── config            → Configuración adicional si aplica
```

---

## 🧪 Pruebas Unitarias

Se realizaron tests con cobertura en:

- ✅ `AlumnoRepositoryTest`
- ✅ `AlumnoServiceImplTest`
- ✅ `AlumnoControllerTest`

Herramientas usadas:
- `@DataR2dbcTest`
- `StepVerifier`
- `WebTestClient`
- `Mockito`

---

## 🖥️ Ejecución del Proyecto

### 🧱 Requisitos

- JDK 17+
- Maven 3.8+

### ▶️ Ejecutar

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 🧪 Probar endpoints

#### Crear alumno (POST)

```http
POST /alumnos
Content-Type: application/json

{
  "id": "a003",
  "nombre": "Luis",
  "apellido": "Ramirez",
  "estado": "activo",
  "edad": 30
}
```

#### Obtener activos (GET)

```
GET /alumnos/activos
```

---

## 📸 Capturas o Demo

Puedes ver la guía de ejecución paso a paso en el documento PDF `demo_reactivo_scotiabank.pdf`.

---

## ✅ Resultado

✔ Todos los tests pasaron  
✔ Validaciones funcionales  
✔ Persistencia reactiva con H2 y R2DBC  
✔ Código limpio y organizado

---

## 🧑‍💻 Autor

**Elmer Puma **  
Desarrollador Fullstack & Backend