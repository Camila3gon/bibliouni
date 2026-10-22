# Escenarios de prueba narrativos – BiblioUni

**Autor:** Camila Gonzalez Rodriguez · **Unidad 1 – Pruebas de software**

Formato: **Dado** (precondición) – **Cuando** (acción) – **Entonces** (resultado esperado). Estos escenarios guiarán el diseño formal de casos de prueba de la Unidad 2.

> Los datos de prueba, nombres de clases y códigos HU son referenciales; ajústelos a su implementación.

## 1. Resumen

| ID | Componente bajo prueba | Descripción del escenario | Tipo | Prioridad | Camino |
|---|---|---|---|---|---|
| EP-001 | `PrestamoService` | Un estudiante no puede tener más de 3 préstamos activos | Unitaria | Alta | Triste / límite |
| EP-002 | `MaterialService` | Registrar un material con datos válidos | Unitaria | Alta | Feliz |
| EP-003 | `PrestamoService` | Prestar un material disponible a un usuario existente | Unitaria | Alta | Feliz |
| EP-004 | `PrestamoController` | Rechazar el préstamo de un material no disponible | API | Alta | Triste |
| EP-005 | `PrestamoService` | Devolver un material y liberarlo para nuevos préstamos | Unitaria | Alta | Feliz |
| EP-006 | `PrestamoController` | Préstamo con un usuario inexistente | API | Alta | Triste |
| EP-007 | `MaterialController` | Registrar un material con datos inválidos | API | Media | Triste |
| EP-008 | `PrestamoRepository` | Persistir un préstamo y consultar los activos por usuario | Integración | Media | Feliz |
| EP-009 | `RecomendacionService` (gancho) | Recomendar materiales según el historial del usuario | Unitaria | Alta | Feliz |
| EP-010 | `RecomendacionController` (gancho) | Consultar recomendaciones de un usuario inexistente | API | Media | Triste |
| EP-011 | `MaterialController` | Consultar el catálogo con paginación | API | Media | Alternativo |
| EP-012 | Flujo completo (sistema) | Prestar y devolver un material de extremo a extremo | Integración | Alta | Feliz |
| EP-013 | `MaterialController` | Acceso a creación de material sin autorización | API | Media | Triste (seguridad) |

## 2. Detalle

### EP-001 · Límite de préstamos activos
- **Componente:** `PrestamoService` · **Tipo:** Unitaria · **Prioridad:** Alta
- **Dado** un estudiante con 3 préstamos activos y un material disponible
- **Cuando** solicita un cuarto préstamo
- **Entonces** el sistema rechaza la operación con una excepción de negocio y no crea el préstamo.
- **Datos:** estudiante E001 con 3 préstamos; clases de equivalencia: 0–2 (acepta), 3 (rechaza). Valores límite: 2 y 3.
- **Trazabilidad:** HU-02.

### EP-002 · Registrar material válido
- **Componente:** `MaterialService` · **Tipo:** Unitaria · **Prioridad:** Alta
- **Dado** un material con título, autor e ISBN válidos
- **Cuando** se registra
- **Entonces** se guarda con estado "disponible" y se devuelve con identificador asignado.
- **Datos:** "Clean Code", "Robert C. Martin", ISBN 9780132350884. **Trazabilidad:** HU-01.

### EP-003 · Préstamo exitoso
- **Componente:** `PrestamoService` · **Tipo:** Unitaria · **Prioridad:** Alta
- **Dado** un usuario existente con menos de 3 préstamos y un material disponible
- **Cuando** se solicita el préstamo
- **Entonces** el préstamo queda activo con fecha de devolución definida y el material pasa a "prestado".
- **Trazabilidad:** HU-02.

### EP-004 · Material no disponible (camino triste)
- **Componente:** `PrestamoController` · **Tipo:** API · **Prioridad:** Alta
- **Dado** un material que ya está prestado
- **Cuando** se envía `POST /api/prestamos` para ese material
- **Entonces** responde **409 Conflict** (o 400) con un mensaje claro y no se crea ningún préstamo.
- **Trazabilidad:** HU-02.

### EP-005 · Devolución
- **Componente:** `PrestamoService` · **Tipo:** Unitaria · **Prioridad:** Alta
- **Dado** un préstamo activo
- **Cuando** se registra la devolución
- **Entonces** el préstamo se cierra, el material vuelve a "disponible" y el usuario recupera cupo de préstamo.
- **Trazabilidad:** HU-03.

### EP-006 · Usuario inexistente (camino triste)
- **Componente:** `PrestamoController` · **Tipo:** API · **Prioridad:** Alta
- **Dado** un identificador de usuario que no existe
- **Cuando** se intenta registrar un préstamo
- **Entonces** responde **404 Not Found** con mensaje descriptivo y no cambia el estado del material.
- **Datos:** usuarioId = 99999. **Trazabilidad:** HU-04.

### EP-007 · Datos inválidos (camino triste)
- **Componente:** `MaterialController` · **Tipo:** API · **Prioridad:** Media
- **Dado** un cuerpo JSON con título vacío e ISBN con formato incorrecto
- **Cuando** se envía `POST /api/materiales` con la cabecera `X-Rol: BIBLIOTECARIO`
- **Entonces** responde **400 Bad Request** indicando los campos inválidos y no se guarda nada.
- **Clases de equivalencia:** título vacío, título nulo, ISBN corto, ISBN con letras. **Trazabilidad:** HU-01.

### EP-008 · Persistencia de préstamos
- **Componente:** `PrestamoRepository` · **Tipo:** Integración (`@DataJpaTest`) · **Prioridad:** Media
- **Dado** un usuario con dos préstamos activos y uno cerrado en la BD de prueba
- **Cuando** se consultan los préstamos activos del usuario
- **Entonces** retorna exactamente 2 registros.
- **Trazabilidad:** HU-02.

### EP-009 · Recomendación de materiales (gancho innovador)
- **Componente:** `RecomendacionService` · **Tipo:** Unitaria · **Prioridad:** Alta
- **Dado** un usuario que ha prestado varios materiales de la categoría "Programación"
- **Cuando** solicita recomendaciones
- **Entonces** recibe materiales disponibles de esa categoría que no ha prestado antes, ordenados por relevancia, y ninguno no disponible.
- **Variante:** con historial vacío recibe una lista por defecto, sin error. **Trazabilidad:** HU-05.

### EP-010 · Recomendaciones para usuario inexistente (gancho + camino triste)
- **Componente:** `RecomendacionController` · **Tipo:** API · **Prioridad:** Media
- **Dado** un usuario que no existe
- **Cuando** se consulta `GET /api/recomendaciones/99999`
- **Entonces** responde **404** con mensaje claro y sin cuerpo de recomendaciones.
- **Trazabilidad:** HU-05.

### EP-011 · Catálogo paginado
- **Componente:** `MaterialController` · **Tipo:** API · **Prioridad:** Media
- **Dado** un catálogo con 25 materiales
- **Cuando** se consulta `GET /api/materiales?page=0&size=10`
- **Entonces** devuelve 10 elementos y los metadatos de paginación (total 25, 3 páginas).
- **Valores límite:** página 2 (5 elementos), página fuera de rango (lista vacía). **Trazabilidad:** HU-01.

### EP-012 · Flujo completo
- **Componente:** sistema (controlador → servicio → BD) · **Tipo:** Integración · **Prioridad:** Alta
- **Dado** un usuario y un material registrados
- **Cuando** presta el material y luego lo devuelve
- **Entonces** el estado del material pasa de disponible → prestado → disponible y queda historial de ambos eventos.
- **Trazabilidad:** HU-02, HU-03.

### EP-013 · Autorización (no funcional: seguridad)
- **Componente:** `MaterialController` · **Tipo:** API · **Prioridad:** Media
- **Dado** un usuario con rol estudiante (cabecera `X-Rol: ESTUDIANTE`)
- **Cuando** intenta crear o eliminar un material
- **Entonces** recibe **403 Forbidden** y el catálogo no cambia.
- **Trazabilidad:** requisito de seguridad.

## 3. Verificación de requisitos de la actividad
- ≥ 10 escenarios: **13** ✔
- ≥ 2 del gancho innovador: **EP-009, EP-010** ✔
- ≥ 2 de camino triste: **EP-001, EP-004, EP-006, EP-007, EP-010, EP-013** ✔
- Escenario no funcional incluido: **EP-013** (seguridad) ✔
