package dev.michel.accountservice.client;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.IssuerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Interfaz que permite el uso de Feign para acceder al servicio movement-service.
 */
@FeignClient(name = "movement-service")
public interface MovementClient {

    /**
     * Obtiene la lista de emisores para una cuenta solicitada
     *
     * @param accountId ID de la cuenta a consultar
     * @return Lista de emisores
     */
    @GetMapping(value = "/movements/{accountId}")
    ResponseEntity<List<IssuerResponse>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId);

    /**
     * Obtiene la lista de emisores para una cuenta solicitada después de crear una operación
     *
     * @param accountId     ID de la cuenta a consultar
     * @param issuerRequest Operación a realizar
     * @return Lista de emisores
     */
    @PostMapping(value = "/movements/{accountId}")
    ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody IssuerRequest issuerRequest);

}
