# Respuestas del taller guiado · Pruebas unitarias del Service

Proyecto: **API de préstamo de bicicletas** (`prestamos-api`).

---

## Misión 1 · ¿Por qué probar?

> Ajusta los números a tu caso real si quieres.

1. **¿Cuántas peticiones tiene tu colección de Postman?**
   5 peticiones: listar, buscar por id, crear, actualizar y eliminar préstamos.
2. **Si cambias una regla de tu Service, ¿cuántas tendrías que repetir para estar seguro de que nada se rompió?**
   Las 5, porque todas pasan por el Service y cualquier cambio puede afectar a otra (regresión).
3. **¿Cuántos cambios crees que le harás al proyecto antes de diciembre? Multiplica.**
   Unos 20 cambios × 5 peticiones = **100 pruebas manuales en Postman**. Con pruebas automáticas es un solo comando que tarda segundos.

---

## Misión 2 · Predice

**Si cambias el valor esperado del `isEqualTo` por uno incorrecto, ¿qué mensaje mostrará la prueba?**

La prueba se pone en rojo con un mensaje de AssertJ como este:

```
expected: "BIC-999"
 but was: "BIC-020"
```

Es decir: lo que yo esperaba (`expected`) y lo que realmente devolvió el código (`but was`).

---

## Misión 3 · Tabla del Service

| Pregunta | Respuesta |
|---|---|
| ¿Cómo se llama tu Service? | `PrestamoService` |
| ¿Qué dependencias recibe en el constructor? | `PrestamoRepository` |
| ¿Cuál de ellas habla con la base de datos? | `PrestamoRepository` (extiende `JpaRepository`, habla con PostgreSQL) |
| ¿Qué método lanza un error cuando el recurso no existe? ¿Qué excepción lanza? | `buscarPorId` (y también `actualizar` y `eliminar`). Lanzan `ResponseStatusException` con `HttpStatus.NOT_FOUND` (404) |
| ¿Qué regla de negocio aplica tu Service? | Todo préstamo nuevo empieza en estado `ACTIVO` y con la fecha/hora actual (`LocalDateTime.now()`) |

---

## Misión 5 · Predice

**¿Qué valor tendrá `respuesta.id()`?**

`null`. El préstamo se creó con `new` en la prueba y nunca se guardó en la base de datos, así que nadie le generó un id (el id lo genera PostgreSQL con `IDENTITY`). El mock solo devuelve el objeto tal cual.

**Experimento (PostgreSQL apagado):** la prueba sigue en verde, porque usa un mock del Repository y nunca se conecta a la base.

---

## Misión 6 · Predice

**Si borras el `orElseThrow` de tu Service, ¿qué pasa con esta prueba?**

La prueba falla (rojo). Sin el `orElseThrow` el Service ya no lanza `ResponseStatusException`: por ejemplo con `.orElse(null)` lo que salta es un `NullPointerException` al convertir a DTO, y `assertThrows` reporta algo como:

```
Unexpected exception type thrown,
expected: <org.springframework.web.server.ResponseStatusException>
 but was: <java.lang.NullPointerException>
```

Así la prueba detecta que el 404 dejó de funcionar.

---

## Misión 7 · Predice

**Si alguien cambia la regla en el Service (por ejemplo, el estado inicial), ¿quién se da cuenta: Postman o tu prueba?**

**La prueba.** Si el estado inicial se cambia a `FINALIZADO`, `crear_prestamoNuevo_quedaActivoYConFechaActual` falla de inmediato con `expected: ACTIVO but was: FINALIZADO`. Postman solo se daría cuenta si alguien vuelve a probar a mano y se fija en ese campo.

---

## Preguntas de reflexión (van en el Pull Request)

**1. ¿Qué regla de tu Service le haría más daño al usuario si se rompiera? ¿Ya tiene prueba?**
La regla de que un préstamo nuevo quede `ACTIVO` y con la fecha actual: si quedara en otro estado o con una fecha incorrecta, la bicicleta aparecería como disponible estando prestada o se calcularía mal el tiempo de uso. Sí tiene prueba: `crear_prestamoNuevo_quedaActivoYConFechaActual`.

**2. ¿Por qué tus pruebas funcionan aunque PostgreSQL esté apagado?**
Porque son pruebas unitarias: no usan `@SpringBootTest` ni arrancan Spring. El `PrestamoRepository` real se reemplaza por un mock de Mockito (`@Mock`) que responde lo que le programamos y nunca se conecta a la base de datos.

**3. ¿En qué se diferencia `when(...)` de `verify(...)`?**
- `when(...).thenReturn(...)` va en el **Arrange**: programa *lo que el mock debe responder* cuando lo llamen (entrada de la prueba).
- `verify(...)` va en el **Assert**: comprueba *cómo fue llamado* el mock después de ejecutar el método (por ejemplo, que se llamó `delete` con el préstamo correcto o que `never()` se llamó). Sirve para métodos que no devuelven nada.

---

## Casos probados (lista para el PR)

| Clase | Prueba | Tipo |
|---|---|---|
| `PrestamoEntityTest` | `actualizar_conPrestamoExistente_cambiaCodigoYEstado` | Entity |
| `PrestamoResponseTest` | `desde_conEntity_copiaTodosLosDatos` | DTO |
| `PrestamoServiceTest` | `buscarPorId_existente_devuelveElPrestamo` | Camino feliz |
| `PrestamoServiceTest` | `buscarPorId_inexistente_lanza404` | Camino de error |
| `PrestamoServiceTest` | `crear_prestamoNuevo_quedaActivoYConFechaActual` | Regla de negocio |
| `PrestamoServiceTest` | `eliminar_existente_borraElPrestamoEncontrado` | `verify` |
| `PrestamoServiceTest` | `eliminar_inexistente_noBorraNada` | `verify(never())` |
| `PrestamoServiceTest` | `actualizar_inexistente_noGuardaNada` | `verify(never())` |

Total: **8 pruebas** (el mínimo pedido son 5).
