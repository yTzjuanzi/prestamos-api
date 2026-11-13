package co.edu.autonoma.prestamos.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PrestamoEntityTest {

    @Test
    void actualizar_conPrestamoExistente_cambiaCodigoYEstado() {
        // Arrange
        PrestamoEntity prestamo = new PrestamoEntity("BIC-014",
                LocalDateTime.now(), EstadoPrestamo.ACTIVO);

        // Act
        prestamo.actualizar("BIC-020", EstadoPrestamo.FINALIZADO);

        // Assert
        assertThat(prestamo.getCodigoBicicleta()).isEqualTo("BIC-020");
        assertThat(prestamo.getEstado()).isEqualTo(EstadoPrestamo.FINALIZADO);
    }
}
