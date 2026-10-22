# BiblioUni

API REST (Spring Boot 3, Java 17, H2 en memoria) para gestionar el préstamo de material bibliográfico universitario.

## Requisitos
- JDK 17 o superior
- Maven 3.9+

## Ejecutar la aplicación
```bash
mvn spring-boot:run
```
La API queda en `http://localhost:8080`.

## Endpoints
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/materiales` | Registrar material (requiere cabecera `X-Rol: BIBLIOTECARIO`) |
| GET | `/api/materiales?page=0&size=10` | Catálogo paginado |
| GET | `/api/materiales/{id}` | Consultar material |
| POST | `/api/usuarios` | Registrar usuario |
| GET | `/api/usuarios/{id}` | Consultar usuario |
| POST | `/api/prestamos` | Prestar material (`usuarioId`, `materialId`) |
| POST | `/api/prestamos/{id}/devolucion` | Devolver material |
| GET | `/api/recomendaciones/{usuarioId}` | Gancho innovador: materiales recomendados |

## Reglas de negocio
- Máximo **3 préstamos activos** por usuario.
- Un material prestado no se puede volver a prestar hasta su devolución.
- Duración del préstamo: 7 días.

## Pruebas
```bash
mvn test
```
Estructura: `src/test/java/com/bibliouni/{servicio,controlador}`. La estrategia de pruebas está en `docs/pruebas/estrategia.md`.
