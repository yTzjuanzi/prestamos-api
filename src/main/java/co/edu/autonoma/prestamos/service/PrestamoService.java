package co.edu.autonoma.prestamos.service;

import co.edu.autonoma.prestamos.domain.EstadoPrestamo;
import co.edu.autonoma.prestamos.domain.PrestamoEntity;
import co.edu.autonoma.prestamos.dto.PrestamoRequest;
import co.edu.autonoma.prestamos.dto.PrestamoResponse;
import co.edu.autonoma.prestamos.dto.PrestamoUpdateRequest;
import co.edu.autonoma.prestamos.repository.PrestamoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository repository;

    public PrestamoService(PrestamoRepository repository) {
        this.repository = repository;
    }

    public List<PrestamoResponse> listar() {
        return repository.findAll().stream()
                .map(PrestamoResponse::desde)
                .toList();
    }

    public PrestamoResponse buscarPorId(Long id) {
        return PrestamoResponse.desde(obtener(id));
    }

    /** Regla de negocio: todo prestamo nuevo empieza ACTIVO y con la fecha de ahora. */
    public PrestamoResponse crear(PrestamoRequest request) {
        PrestamoEntity nuevo = new PrestamoEntity(
                request.codigoBicicleta(),
                LocalDateTime.now(),
                EstadoPrestamo.ACTIVO);
        return PrestamoResponse.desde(repository.save(nuevo));
    }

    public PrestamoResponse actualizar(Long id, PrestamoUpdateRequest request) {
        PrestamoEntity prestamo = obtener(id);
        prestamo.actualizar(request.codigoBicicleta(), request.estado());
        return PrestamoResponse.desde(repository.save(prestamo));
    }

    public void eliminar(Long id) {
        PrestamoEntity prestamo = obtener(id);
        repository.delete(prestamo);
    }

    private PrestamoEntity obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Prestamo " + id + " no encontrado"));
    }
}
