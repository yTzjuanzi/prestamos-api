package co.edu.autonoma.prestamos.dto;

import co.edu.autonoma.prestamos.domain.EstadoPrestamo;
import co.edu.autonoma.prestamos.domain.PrestamoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PrestamoResponseTest {

    @Test
    void desde_conEntity_copiaTodosLosDatos() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2026, 11, 12, 8, 30);
        PrestamoEntity prestamo = new PrestamoEntity("BIC-014", inicio, EstadoPrestamo.ACTIVO);

        // Act
        PrestamoResponse respuesta = PrestamoResponse.desde(prestamo);

        // Assert
        assertThat(respuesta.codigoBicicleta()).isEqualTo("BIC-014");
        assertThat(respuesta.inicio()).isEqualTo(inicio);
        assertThat(respuesta.estado()).isEqualTo(EstadoPrestamo.ACTIVO);
        assertThat(respuesta.id()).isNull(); // nunca se guardo en la base, no tiene id
    }
}
