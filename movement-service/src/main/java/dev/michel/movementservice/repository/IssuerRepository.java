package dev.michel.movementservice.repository;

import dev.michel.movementservice.entity.IssuerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio en el que se almacenan las acciones de cada usuario
 */
public interface IssuerRepository extends JpaRepository<IssuerRequest, Long> {
    /**
     * Método que permite buscar las acciones de un usuario especifico
     *
     * @param accountId ID de la cuenta
     * @return Lista de acciones de un usuario
     */
    List<IssuerRequest> findAllByAccountId(Long accountId);

    /**
     * @param accountId  ID de la cuenta
     * @param issuerName Nombre del emisor de las acciones
     * @return Acciones del usuario para el emisor correspondiente
     */
    IssuerRequest findByAccountIdAndIssuerName(Long accountId, String issuerName);
}
