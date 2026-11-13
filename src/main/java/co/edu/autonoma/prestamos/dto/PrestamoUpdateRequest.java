package co.edu.autonoma.prestamos.dto;

import co.edu.autonoma.prestamos.domain.EstadoPrestamo;

public record PrestamoUpdateRequest(String codigoBicicleta, EstadoPrestamo estado) {
}
