package dev.michel.accountservice.client;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.IssuerResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa un circuitbreaker para el cliente Feign MovementClient
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class MovementClientCircuitBreaker {

    private final MovementClient movementClient;

    /**
     * Obtiene la lista de emisores para una cuenta solicitada
     *
     * @param accountId ID de la cuenta a consultar
     * @return Lista de emisores
     */
    @Bulkhead(name = "/movements/{accountId}", fallbackMethod = "movementsFallback")
    public ResponseEntity<List<IssuerResponse>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId) {
        log.info("GET /movements/{}", accountId);
        return movementClient.getAllIssuersByAccountId(accountId);
    }

    /**
     * Obtiene la lista de emisores para una cuenta solicitada después de crear una operación
     *
     * @param accountId     ID de la cuenta a consultar
     * @param issuerRequest Operación a realizar
     * @return Lista de emisores
     */
    @Bulkhead(name = "/movements/{accountId}", fallbackMethod = "movementsFallback")
    public ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @RequestBody IssuerRequest issuerRequest) {
        log.info("POST /movements/{}, operación: {}", accountId, issuerRequest);
        return movementClient.createMovement(accountId, issuerRequest);
    }

    /**
     * Este método se llama únicamente cuando el cliente Feign detecta un error, se utiliza como respuesta alternativa.
     *
     * @param accountId ID de la cuenta a consultar
     * @param throwable Excepción generada por el cliente Feign
     * @return Lista de emisores vacía como respuesta alternativa con código de estado INTERNAL_SERVER_ERROR
     */
    public ResponseEntity<List<IssuerResponse>> movementsFallback(Long accountId, Throwable throwable) {
        log.error("FALLBACK GET /movements/{}, Se retorna lista vacía, respuesta parcial, error: {}", accountId, throwable.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }

    /**
     * Este método se llama únicamente cuando el cliente Feign detecta un error, se utiliza como respuesta alternativa.
     *
     * @param accountId     ID de la cuenta a consultar
     * @param issuerRequest Operación a realizar
     * @param throwable     Excepción generada por el cliente Feign
     * @return Lista de emisores vacía como respuesta alternativa con código de estado INTERNAL_SERVER_ERROR
     */
    public ResponseEntity<List<IssuerResponse>> movementsFallback(Long accountId, IssuerRequest issuerRequest, Throwable throwable) {
        log.error("FALLBACK POST /movements/{}, operación: {} Se retorna lista vacía, respuesta parcial, error: {}", accountId, issuerRequest, throwable.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
    }
}
