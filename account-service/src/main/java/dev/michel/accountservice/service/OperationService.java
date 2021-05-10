package dev.michel.accountservice.service;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.OperationResponse;
import org.springframework.http.ResponseEntity;

/**
 * Servicio que permite realizar operaciones en las cuentas
 */
public interface OperationService {
    /**
     * Método que realiza una operación
     *
     * @param id            ID de la cuenta que realizará la operación
     * @param issuerRequest Datos de la operación a realizar
     * @return ResponseEntity<OperationResponse> con los resultados de la operación y el código de estado.
     */
    ResponseEntity<OperationResponse> createOperation(Long id, IssuerRequest issuerRequest);
}
