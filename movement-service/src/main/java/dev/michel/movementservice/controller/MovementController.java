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
        return ResponseEntity.ok(issuerService.getAllIssuersByAccountId(accountId));
    }

    @PostMapping(value = "/{accountId}")
    @ApiOperation("Obtiene la lista de emisores para una cuenta solicitada después de crear una operación")
    public ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody IssuerRequest issuerRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        issuerRequest.setAccountId(accountId);
        List<IssuerResponse> actualIssuerResponse = issuerService.createIssuer(issuerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualIssuerResponse);
    }
}
