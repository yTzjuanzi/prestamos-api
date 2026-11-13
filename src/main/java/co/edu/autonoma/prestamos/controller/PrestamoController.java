package co.edu.autonoma.prestamos.controller;

import co.edu.autonoma.prestamos.dto.PrestamoRequest;
import co.edu.autonoma.prestamos.dto.PrestamoResponse;
import co.edu.autonoma.prestamos.dto.PrestamoUpdateRequest;
import co.edu.autonoma.prestamos.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PrestamoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PrestamoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponse crear(@RequestBody PrestamoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public PrestamoResponse actualizar(@PathVariable Long id, @RequestBody PrestamoUpdateRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
