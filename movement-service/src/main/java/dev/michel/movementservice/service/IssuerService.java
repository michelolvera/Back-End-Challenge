package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;

import java.util.List;

/**
 * Servicio que permite la administración de acciones
 */
public interface IssuerService {
    /**
     * Método que permite crear una Operación en la cuenta de un usuario
     *
     * @param issuerRequest Operación a realizar
     * @return Lista de acciones de un usuario
     */
    List<IssuerResponse> createIssuer(IssuerRequest issuerRequest);

    /**
     * Método que permite obtener todas las acciones de un usuario
     *
     * @param accountId ID de la cuenta
     * @return Lista de acciones de un usuario
     */
    List<IssuerResponse> getAllIssuersByAccountId(Long accountId);
}
