## Pruebas unitarias del Service

**Captura:** _(pega aquí la captura del BUILD SUCCESS)_

### Casos probados
- `PrestamoEntityTest.actualizar_conPrestamoExistente_cambiaCodigoYEstado` (Entity)
- `PrestamoResponseTest.desde_conEntity_copiaTodosLosDatos` (DTO)
- `PrestamoServiceTest.buscarPorId_existente_devuelveElPrestamo` (camino feliz)
- `PrestamoServiceTest.buscarPorId_inexistente_lanza404` (camino de error)
- `PrestamoServiceTest.crear_prestamoNuevo_quedaActivoYConFechaActual` (regla de negocio)
- `PrestamoServiceTest.eliminar_existente_borraElPrestamoEncontrado` (verify)
- `PrestamoServiceTest.eliminar_inexistente_noBorraNada` (verify never)
- `PrestamoServiceTest.actualizar_inexistente_noGuardaNada` (verify never)

### Preguntas de reflexión

**1. ¿Qué regla de tu Service le haría más daño al usuario si se rompiera? ¿Ya tiene prueba?**
Que un préstamo nuevo quede `ACTIVO` y con la fecha actual. Si se rompe, una bicicleta prestada podría aparecer como disponible o el tiempo de uso se calcularía mal. Sí tiene prueba: `crear_prestamoNuevo_quedaActivoYConFechaActual`.

**2. ¿Por qué tus pruebas funcionan aunque PostgreSQL esté apagado?**
Porque son unitarias: no usan `@SpringBootTest` ni arrancan Spring. El Repository real se reemplaza por un mock de Mockito que responde lo que le programamos y nunca se conecta a la base.

**3. ¿En qué se diferencia `when(...)` de `verify(...)`?**
`when(...)` va en el Arrange y programa lo que el mock debe responder. `verify(...)` va en el Assert y comprueba cómo fue llamado el mock (por ejemplo, que se llamó `delete` con el préstamo correcto, o que con `never()` no se llamó).
