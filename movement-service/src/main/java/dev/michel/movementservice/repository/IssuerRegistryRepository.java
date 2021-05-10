package dev.michel.movementservice.repository;

import dev.michel.movementservice.entity.IssuerRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio en el que se encuentran los registros de todas las peticiones realizadas a la API
 */
public interface IssuerRegistryRepository extends JpaRepository<IssuerRegistry, Long> {

    /**
     * Método que permite buscar un registro especifico
     *
     * @param accountId   ID de la cuenta
     * @param issuerName  Nombre del emisor de las acciones
     * @param totalShares Cantidad de acciones en la operación
     * @param sharePrice  Precio de cada acción al momento de la operación
     * @param operation   Tipo de operación (BUY | SELL)
     * @return El registro buscado
     */
    IssuerRegistry findByAccountIdAndIssuerNameAndTotalSharesAndSharePriceAndOperation(Long accountId, String issuerName, Integer totalShares, Double sharePrice, String operation);
}
