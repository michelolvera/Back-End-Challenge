package dev.michel.movementservice.controller;

import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;
import dev.michel.movementservice.service.IssuerService;
import dev.michel.movementservice.util.ApiUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class MovementController {

    private final IssuerService issuerService;
    private final ApiUtils apiUtils = new ApiUtils();

    @GetMapping(value = "/{accountId}")
    @ApiOperation("Retorna la lista de emisores para una cuenta solicitada")
    public ResponseEntity<List<IssuerResponse>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId) {
        log.info("Se solicitan los movimientos de la cuenta: {}", accountId);
        List<IssuerResponse> issuerResponses = issuerService.getAllIssuersByAccountId(accountId);
        if (issuerResponses.isEmpty())
            log.warn("La cuenta con id: {} no tiene movimientos.", accountId);
        log.info("La cuenta {} tiene los siguientes movimientos: {}", accountId, issuerResponses);
        return ResponseEntity.ok(issuerService.getAllIssuersByAccountId(accountId));
    }

    @PostMapping(value = "/{accountId}")
    @ApiOperation("Obtiene la lista de emisores para una cuenta solicitada después de crear una operación")
    public ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody IssuerRequest issuerRequest, BindingResult result) {
        log.info("Se solicita crear una operación a la cuenta {}: {}", accountId, issuerRequest);
        if (result.hasErrors()) {
            log.error("La validación de la operacion fallo: {}", apiUtils.formatMessage(result));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        issuerRequest.setAccountId(accountId);
        List<IssuerResponse> actualIssuerResponse = issuerService.createIssuer(issuerRequest);
        log.info("Se retorna la lista de acciones: {}", actualIssuerResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualIssuerResponse);
    }
}
