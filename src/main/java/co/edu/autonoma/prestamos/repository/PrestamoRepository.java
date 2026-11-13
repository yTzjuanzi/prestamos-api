package co.edu.autonoma.prestamos.repository;

import co.edu.autonoma.prestamos.domain.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<PrestamoEntity, Long> {
}
