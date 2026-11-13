package co.edu.autonoma.prestamos.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
public class PrestamoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoBicicleta;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPrestamo estado;

    protected PrestamoEntity() {
        // requerido por JPA
    }

    public PrestamoEntity(String codigoBicicleta, LocalDateTime inicio, EstadoPrestamo estado) {
        this.codigoBicicleta = codigoBicicleta;
        this.inicio = inicio;
        this.estado = estado;
    }

    public void actualizar(String codigoBicicleta, EstadoPrestamo estado) {
        this.codigoBicicleta = codigoBicicleta;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoBicicleta() {
        return codigoBicicleta;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }
}
