package co.edu.autonoma.prestamos.service;

import co.edu.autonoma.prestamos.domain.EstadoPrestamo;
import co.edu.autonoma.prestamos.domain.PrestamoEntity;
import co.edu.autonoma.prestamos.dto.PrestamoRequest;
import co.edu.autonoma.prestamos.dto.PrestamoResponse;
import co.edu.autonoma.prestamos.dto.PrestamoUpdateRequest;
import co.edu.autonoma.prestamos.repository.PrestamoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private PrestamoRepository repository;

    @InjectMocks
    private PrestamoService service;

    // ---------- Mision 5: camino feliz ----------
    @Test
    void buscarPorId_existente_devuelveElPrestamo() {
        // Arrange
        PrestamoEntity prestamo = new PrestamoEntity("BIC-014",
                LocalDateTime.now(), EstadoPrestamo.ACTIVO);
        when(repository.findById(3L)).thenReturn(Optional.of(prestamo));

        // Act
        PrestamoResponse respuesta = service.buscarPorId(3L);

        // Assert
        assertThat(respuesta.codigoBicicleta()).isEqualTo("BIC-014");
        assertThat(respuesta.estado()).isEqualTo(EstadoPrestamo.ACTIVO);
    }

    // ---------- Mision 6: camino de error ----------
    @Test
    void buscarPorId_inexistente_lanza404() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.buscarPorId(99L));

        // Assert
        assertThat(error.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(error.getReason()).contains("99");
    }

    // ---------- Mision 7: regla de negocio ----------
    @Test
    void crear_prestamoNuevo_quedaActivoYConFechaActual() {
        // Arrange: save devuelve el mismo objeto que recibe, como haria la base
        when(repository.save(any(PrestamoEntity.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Act
        PrestamoResponse respuesta = service.crear(new PrestamoRequest("BIC-014"));

        // Assert
        assertThat(respuesta.codigoBicicleta()).isEqualTo("BIC-014");
        assertThat(respuesta.estado()).isEqualTo(EstadoPrestamo.ACTIVO);
        assertThat(respuesta.inicio())
                .isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
    }

    // ---------- Mision 8: verificar interacciones ----------
    @Test
    void eliminar_existente_borraElPrestamoEncontrado() {
        PrestamoEntity prestamo = new PrestamoEntity("BIC-014",
                LocalDateTime.now(), EstadoPrestamo.ACTIVO);
        when(repository.findById(3L)).thenReturn(Optional.of(prestamo));

        service.eliminar(3L);

        verify(repository).delete(prestamo);
    }

    @Test
    void eliminar_inexistente_noBorraNada() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.eliminar(99L));

        verify(repository, never()).delete(any());
    }

    @Test
    void actualizar_inexistente_noGuardaNada() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.actualizar(99L, new PrestamoUpdateRequest("BIC-020", EstadoPrestamo.FINALIZADO)));

        verify(repository, never()).save(any());
    }
}
