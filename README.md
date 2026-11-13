# prestamos-api

API REST de préstamo de bicicletas (Spring Boot 3 + JPA + PostgreSQL) con pruebas unitarias del Service usando JUnit 5, Mockito y AssertJ.

## Estructura

```
src/main/java/co/edu/autonoma/prestamos/
├── domain/      PrestamoEntity, EstadoPrestamo
├── dto/         PrestamoRequest, PrestamoUpdateRequest, PrestamoResponse
├── repository/  PrestamoRepository
├── service/     PrestamoService
└── controller/  PrestamoController   (/api/prestamos)

src/test/java/co/edu/autonoma/prestamos/
├── domain/      PrestamoEntityTest
├── dto/         PrestamoResponseTest
└── service/     PrestamoServiceTest
```

## Ejecutar las pruebas

No necesitan PostgreSQL encendido:

```bash
mvn test -Dtest="*Test"
```

Al final debe aparecer `Failures: 0, Errors: 0` y `BUILD SUCCESS`.

## Ejecutar la API

Necesita PostgreSQL con una base `prestamos` y la variable `DB_PASSWORD`:

```bash
mvn spring-boot:run
```
