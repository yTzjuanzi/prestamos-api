package co.edu.autonoma.prestamos.dto;

import co.edu.autonoma.prestamos.domain.EstadoPrestamo;
import co.edu.autonoma.prestamos.domain.PrestamoEntity;

import java.time.LocalDateTime;

public record PrestamoResponse(Long id, String codigoBicicleta, LocalDateTime inicio, EstadoPrestamo estado) {

    public static PrestamoResponse desde(PrestamoEntity entity) {
        return new PrestamoResponse(
                entity.getId(),
                entity.getCodigoBicicleta(),
                entity.getInicio(),
                entity.getEstado());
    }
}
