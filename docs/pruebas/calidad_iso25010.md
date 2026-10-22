# Evaluación de características de calidad ISO/IEC 25010 – BiblioUni

**Autor:** Camila Gonzalez Rodriguez · **Unidad 1 – Pruebas de software**

Escala de relevancia: **Alta / Media / Baja** según el impacto de un defecto en BiblioUni (sistema de préstamos de material bibliográfico con API REST).

## 1. Tabla de evaluación

| # | Característica | Definición (aplicada a BiblioUni) | Subcaracterísticas relevantes | Relevancia | Justificación | Ejemplo de medición en BiblioUni | Técnica de prueba | Umbral aceptable |
|---|---|---|---|---|---|---|---|---|
| 1 | **Funcionalidad** (adecuación funcional) | El sistema hace lo que los requisitos piden: prestar, devolver, consultar, aplicar reglas. | Completitud, corrección, pertinencia funcional | **Alta** | Es la razón de ser del sistema; un error en las reglas de préstamo invalida el producto. | % de HU con pruebas aprobadas; defectos funcionales por módulo. | Unitarias, integración y sistema (caja negra) | 100 % HU críticas aprobadas |
| 2 | **Rendimiento** (eficiencia de desempeño) | Tiempos de respuesta y uso de recursos adecuados. | Comportamiento temporal, uso de recursos | **Baja** | Pocos usuarios concurrentes; consultas simples. | Tiempo de respuesta de `GET /api/materiales` con 1 000 registros. | Medición básica con MockMvc/Postman (sin carga) | < 500 ms por petición |
| 3 | **Compatibilidad** | Convive e intercambia información con otros sistemas. | Interoperabilidad, coexistencia | **Media** | Los clientes consumen JSON vía HTTP; el contrato debe ser estable. | Validación del esquema JSON de respuestas. | Pruebas de API (contrato) | 100 % de respuestas con esquema válido |
| 4 | **Usabilidad** (capacidad de interacción) | Facilidad de uso y claridad de mensajes. | Capacidad de reconocimiento, protección frente a errores de usuario | **Media** | Sin interfaz propia, pero los mensajes de error de la API deben ser claros. | % de errores con mensaje descriptivo y código HTTP correcto. | Pruebas de API de camino triste | 100 % de errores con mensaje claro |
| 5 | **Fiabilidad** | Funciona de forma consistente y se recupera de fallos. | Madurez, tolerancia a fallos, recuperabilidad | **Alta** | Perder o duplicar un préstamo afecta a los usuarios y la integridad del inventario. | Tasa de éxito de transacciones de préstamo; consistencia tras errores. | Integración transaccional, pruebas de errores | 0 inconsistencias en el inventario |
| 6 | **Seguridad** | Protege datos y controla acceso. | Confidencialidad, integridad, autenticidad, no repudio | **Alta** | Maneja datos personales de estudiantes; solo bibliotecarios deben modificar el catálogo. | Intentos de acceso no autorizado rechazados. | MockMvc con cabecera `X-Rol`, validación de entradas | 100 % de accesos no autorizados rechazados |
| 7 | **Mantenibilidad** | Facilidad para modificar y probar el código. | Modularidad, reusabilidad, capacidad de ser probado | **Media** | El proyecto evolucionará entre módulos; una buena separación en capas facilita las pruebas. | Cobertura de pruebas; complejidad ciclomática. | Análisis estático, JaCoCo, revisión de código | Cobertura ≥ 70 % |
| 8 | **Portabilidad** | Se instala y ejecuta en distintos entornos. | Adaptabilidad, facilidad de instalación | **Baja** | Java/Spring Boot corre en cualquier SO con JVM. | `mvn test` exitoso en dos sistemas operativos. | Ejecución del build en CI y local | Build exitoso en ambos |

## 2. Las 3 características más críticas y cómo se abordarán

### 2.1 Funcionalidad (adecuación funcional)
- **Por qué:** las reglas de préstamo (límite de 3 préstamos activos, disponibilidad, fechas) son el núcleo del negocio.
- **Cómo se prueba:** unitarias sobre `PrestamoService` y `MaterialService` con partición de equivalencia y valores límite (2, 3, 4 préstamos); pruebas de API para los endpoints; trazabilidad HU → escenario.
- **Métrica:** 100 % de HU críticas con al menos un escenario automatizado en verde.

### 2.2 Fiabilidad
- **Por qué:** una inconsistencia (material prestado dos veces, devolución perdida) genera errores acumulativos.
- **Cómo se prueba:** pruebas de integración con BD en memoria que verifican transacciones (`rollback` ante error) y estados coherentes; pruebas de camino triste (material no disponible, usuario inexistente).
- **Métrica:** 0 estados inconsistentes tras la suite de integración.

### 2.3 Seguridad
- **Por qué:** se almacenan datos personales y hay acciones restringidas por rol.
- **Cómo se prueba:** pruebas de autorización por rol (cabecera `X-Rol`) con MockMvc; validación de entradas (campos nulos, vacíos, demasiado largos, intentos básicos de inyección); verificación de que no se expone información sensible en respuestas de error.
- **Métrica:** 100 % de los endpoints protegidos devuelven 403 sin el rol de bibliotecario.

## 3. Matriz de criticidad (probabilidad × impacto)

| Característica | Probabilidad de defecto | Impacto | Criticidad |
|---|---|---|---|
| Funcionalidad | Alta | Alto | **Crítica** |
| Fiabilidad | Media | Alto | **Crítica** |
| Seguridad | Media | Alto | **Crítica** |
| Mantenibilidad | Media | Medio | Media |
| Usabilidad | Media | Medio | Media |
| Compatibilidad | Baja | Medio | Media |
| Rendimiento | Baja | Bajo | Baja |
| Portabilidad | Baja | Bajo | Baja |

## 4. Trade-offs entre características
- **Seguridad ↔ Usabilidad:** más validaciones y autenticación hacen la API más estricta; se compensa con mensajes de error claros.
- **Fiabilidad ↔ Rendimiento:** las transacciones y verificaciones de consistencia añaden latencia; se acepta porque el volumen es bajo.
- **Funcionalidad ↔ Mantenibilidad:** agregar reglas en los servicios sin pruebas degrada la mantenibilidad; la suite de regresión lo mitiga.

## 5. Referencias
- ISO/IEC 25010 – Systems and software Quality Requirements and Evaluation (SQuaRE) – System and software quality models. Existe una revisión (ISO/IEC 25010:2023) que renombra algunas características (p. ej., "Adecuación funcional", "Eficiencia de desempeño", "Capacidad de interacción"); en este documento se usan los nombres de la guía de la actividad.
- ISTQB, *Certified Tester Foundation Level (CTFL) Syllabus*.
