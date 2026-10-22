# Estrategia de pruebas – BiblioUni

| | |
|---|---|
| **Espacio académico** | Pruebas de software – Unidad 1: Fundamentos de pruebas de software y estándares |
| **Autor** | Camila Gonzalez Rodriguez (camilagonzalezrodriguez251@gmail.com) |
| **Rama** | `feature/estrategia-pruebas` → PR hacia `develop` |
| **Versión** | 1.0 – octubre de 2026 |

> **Nota de alcance:** el documento se basa en la arquitectura de BiblioUni (Spring Boot, API REST, capas modelo / repositorio / servicio / controlador), descrita en el `README.md` del repositorio. El gancho innovador es el módulo de recomendaciones de materiales (ver sección 6).

---

## 1. Objetivos y alcance

**Objetivo general:** garantizar que BiblioUni (sistema de gestión de préstamos de material bibliográfico universitario) cumpla sus requisitos funcionales y los atributos de calidad críticos, detectando defectos lo más temprano posible.

**Objetivos específicos**

1. Definir qué se prueba, con qué tipos de prueba y en qué niveles.
2. Priorizar el esfuerzo según el riesgo de cada componente.
3. Establecer criterios de entrada/salida y métricas de cobertura.
4. Dejar la base para el diseño formal de casos de prueba (Unidad 2) y la automatización (Unidad 3).

**Marco de referencia:** terminología y procesos ISTQB (CTFL); modelo de calidad ISO/IEC 25010.

---

## 2. ¿Qué se va a probar? (componentes bajo prueba)

| Componente | Ejemplos de clases | Qué se verifica | Responsable | Riesgo |
|---|---|---|---|---|
| **Modelo** (entidades/dominio) | `Material`, `Usuario`, `Prestamo` | Validaciones, estados (disponible/prestado), invariantes | Desarrollador de dominio | Medio |
| **Servicios** (lógica de negocio) | `MaterialService`, `PrestamoService`, `UsuarioService` | Reglas de negocio: máx. 3 préstamos activos, disponibilidad, fechas de devolución, multas | Desarrollador backend | **Alto** |
| **Controladores REST** | `MaterialController`, `PrestamoController`, `UsuarioController` | Contratos HTTP: rutas, verbos, códigos de estado, JSON, validación de entrada, manejo de errores | Desarrollador backend / QA | **Alto** |
| **Base de datos / repositorios** | `MaterialRepository`, `PrestamoRepository` | Mapeo JPA, consultas derivadas, restricciones de integridad | QA / Desarrollador | Medio |
| **Gancho innovador** | Ver sección 6 | Ver sección 6 | Todo el equipo | Alto |

> Cada responsable es un rol; asignar nombres del equipo antes de la entrega.

---

## 3. ¿Qué tipos de pruebas aplican y por qué?

| Tipo | Aplica | Justificación | Herramientas |
|---|---|---|---|
| **Unitarias** | Sí | Las reglas de negocio viven en los servicios y se pueden aislar con mocks; son rápidas y baratas. | JUnit 5, Mockito, AssertJ |
| **De integración** | Sí | Hay que comprobar que controlador ↔ servicio ↔ repositorio ↔ BD funcionan juntos (serialización, transacciones, JPA). | `@SpringBootTest`, `@DataJpaTest`, H2 |
| **De API (REST)** | Sí | La API es la interfaz pública del sistema; se validan códigos HTTP, cuerpos y errores. | MockMvc, Postman/Newman |
| **Funcionales (caja negra)** | Sí | Validan que los requisitos/HU se cumplen desde la perspectiva del usuario. | Casos derivados de HU |
| **De regresión** | Sí | Cada cambio puede romper reglas ya probadas; se ejecutan en cada PR. | Suite Maven en CI |
| **No funcionales (básicas)** | Parcial | Se prueban solo las características críticas: fiabilidad y seguridad básica (validación de entrada, autorización por rol). Ver `calidad_iso25010.md`. | JUnit, MockMvc |
| **Estáticas** | Sí | Revisión de código en PR y análisis estático para defectos tempranos. | Revisión por pares, SonarLint |

---

## 4. ¿Qué niveles de pruebas se ejecutarán?

| Nivel (ISTQB) | Objeto | Enfoque | Criterio de entrada | Criterio de salida |
|---|---|---|---|---|
| **Componente / unitario** | Servicios y modelo | Caja blanca + caja negra, dependencias simuladas | Código compila; reglas de negocio documentadas | 100 % de tests pasan; cobertura de líneas ≥ 80 % en servicios; sin defectos críticos abiertos |
| **Integración** | Controladores + servicios + repositorios + BD en memoria | Caja negra sobre la API con MockMvc | Pruebas unitarias en verde; entorno de pruebas configurado | Todos los endpoints con ≥ 1 camino feliz y ≥ 1 camino de error; 100 % de tests en verde |
| **Sistema** | Flujos completos de extremo a extremo (registrar material → prestar → devolver) | Caja negra, contra la aplicación levantada | Integración aprobada; build estable en `develop` | Flujos críticos ejecutados sin defectos de severidad alta |
| **Aceptación** | Historias de usuario con el docente/usuario final | Validación (¿construimos el producto correcto?) | Pruebas de sistema aprobadas | HU críticas aceptadas por el docente |

**Técnicas de diseño:** partición de equivalencia y valores límite (p. ej. 2, 3 y 4 préstamos activos), tablas de decisión para reglas de préstamo, transición de estados del material y adivinanza de errores (datos nulos, IDs inexistentes).

**Métricas esperadas:** cobertura de líneas ≥ 80 % en servicios y ≥ 70 % global (JaCoCo); 100 % de las HU críticas con al menos una prueba; tasa de éxito del pipeline = 100 % antes de fusionar a `develop`.

---

## 5. ¿Qué NO se probará y por qué?

| Excluido | Motivo |
|---|---|
| Pruebas de carga y estrés | El sistema es un proyecto académico con pocos usuarios concurrentes; no hay requisito de rendimiento bajo alta demanda. |
| Pruebas de seguridad avanzadas (pentesting, fuzzing) | Fuera del alcance del curso y de la infraestructura; se cubre solo seguridad básica. |
| Pruebas de compatibilidad con navegadores / dispositivos | BiblioUni expone una API REST; no hay interfaz web propia en este módulo. |
| Pruebas de portabilidad entre sistemas operativos | La JVM ya garantiza portabilidad; riesgo bajo. |
| Código generado por el framework y getters/setters triviales | Bajo valor y alto costo de mantenimiento. |
| Terceros (servidor de BD, librerías Spring) | Se asumen probados por sus proveedores. |

**Riesgo residual aceptado:** degradación de rendimiento con grandes volúmenes de catálogo; se documenta y se revisa si el alcance crece.

---

## 6. Pruebas del gancho innovador

> El gancho innovador de BiblioUni es un **recomendador de materiales**: sugiere (máximo 5) materiales disponibles de las categorías que el usuario más ha prestado, sin repetir los que ya prestó.

| Componente del gancho | Nivel | Tipo | Criterio de prueba |
|---|---|---|---|
| Lógica de recomendación (`RecomendacionService`) | Unitario | Funcional, caja blanca | Con historial conocido devuelve los materiales esperados; con historial vacío devuelve una lista por defecto (no falla); nunca recomienda materiales no disponibles. |
| Endpoint del gancho (`GET /api/recomendaciones/{usuarioId}`) | Integración / API | API | 200 con JSON válido (máx. 5 materiales); 404 si el usuario no existe. |
| Flujo extremo a extremo | Sistema | Funcional | Un usuario presta un material y recibe recomendaciones coherentes a continuación. |

**Criterio de salida del gancho:** todos los escenarios EP-009 y EP-010 en verde y cobertura ≥ 80 % de su servicio.

---

## 7. Análisis de riesgos por componente

| Riesgo | Prob. | Impacto | Mitigación |
|---|---|---|---|
| Préstamo de material ya prestado (condición de carrera) | Media | Alto | Pruebas de servicio sobre disponibilidad + prueba de integración de la restricción |
| Violación del límite de 3 préstamos por estudiante | Media | Alto | Valores límite en `PrestamoService` (EP-001) |
| Respuestas HTTP incorrectas (500 en lugar de 400/404) | Alta | Medio | Pruebas de API de camino triste en todos los controladores |
| Cambios que rompen funcionalidad previa | Alta | Medio | Regresión automática en cada PR |
| Datos inválidos que llegan a la BD | Media | Alto | Validaciones (`@Valid`) + pruebas de datos inválidos |

---

## 8. Matriz de trazabilidad requisito → prueba

| Requisito / HU | Escenarios |
|---|---|
| HU-01 Registrar y consultar material | EP-002, EP-007, EP-011 |
| HU-02 Prestar material | EP-001, EP-003, EP-004, EP-008 |
| HU-03 Devolver material | EP-005, EP-012 |
| HU-04 Gestión de usuarios | EP-006 |
| HU-05 Gancho innovador | EP-009, EP-010 |

> Los códigos HU son referenciales: reemplácelos por los de su backlog.

---

## 9. Entorno y organización del repositorio de pruebas

```
src/test/java/com/bibliouni/
├── servicio/      → pruebas unitarias de servicios
└── controlador/   → pruebas de integración / API
docs/pruebas/
├── estrategia.md
├── mapa_conceptual.png
├── calidad_iso25010.md
└── escenarios.md
```

- Ejecución: `mvn test` (local y en el pipeline del PR).
- Reportes: Surefire (`target/surefire-reports`) y, opcionalmente, JaCoCo.
- Flujo Git: `feature/estrategia-pruebas` → Pull Request → `develop`.
