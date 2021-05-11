package dev.michel.accountservice.controller;

import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.OperationResponse;
import dev.michel.accountservice.service.AccountService;
import dev.michel.accountservice.service.OperationService;
import dev.michel.accountservice.util.ApiUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final OperationService operationService;
    private final ApiUtils apiUtils = new ApiUtils();

    @GetMapping(value = "/{id}")
    @ApiOperation("Retorna una cuenta buscándola por su ID")
    public ResponseEntity<Account> getAccount(@PathVariable("id") Long id) {
        log.info("Se solicita la cuenta: {}", id);
        Account account = accountService.getAccount(id);
        if (account == null){
            log.warn("No existe la cuenta solicitada");
            return ResponseEntity.notFound().build();
        }
        log.info("Se retorna la cuenta: {}", account);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    @ApiOperation("Crea y retorna una nueva cuenta, solo es necesario enviar la cantidad de crédito (cash)")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account, BindingResult result) {
        log.info("Creando nueva cuenta: {}", account);
        if (result.hasErrors()) {
            log.error("La validación de la cuenta fallo: {}", apiUtils.formatMessage(result));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        if (account.getCash() < 0) {
            log.error("La validación de la cuenta fallo: cash: debe ser mayor que 0");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cash: debe ser mayor que 0");
        }
        Account accountCreate = accountService.createAccount(account);
        log.info("Se creo la cuenta: {}", account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreate);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation("Actualiza el saldo de una cuenta, sumando el valor enviado en el atributo cash")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id, @Valid @RequestBody Account account, BindingResult result) {
        log.info("Actualizando cuenta {}, con {}", id, account);
        if (result.hasErrors()) {
            log.error("La validación de la cuenta fallo: {}", apiUtils.formatMessage(result));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        account.setId(id);
        Account accountDB = accountService.updateAccount(account);
        if (accountDB == null){
            log.warn("No existe la cuenta solicitada");
            return ResponseEntity.notFound().build();
        }
        log.info("Cuenta actualizada: {}", accountDB);
        return ResponseEntity.ok(accountDB);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Elimina una cuenta, siempre y cuando esta no cuente con saldo o acciones")
    public ResponseEntity<Account> deleteAccount(@PathVariable("id") Long id) {
        log.info("Se solicita eliminar la cuenta con id: {}", id);
        Account accountDB = accountService.deleteAccount(id);
        if (accountDB == null) {
            log.warn("No existe la cuenta solicitada");
            return ResponseEntity.notFound().build();
        }
        log.info("Cuenta eliminada: {}", accountDB);
        return ResponseEntity.ok(accountDB);
    }

    @PostMapping(value = "/{id}/orders")
    @ApiOperation("Genera una operación de compra o venta en una cuenta")
    public ResponseEntity<OperationResponse> createIssuer(@PathVariable("id") Long id, @Valid @RequestBody IssuerRequest issuerRequest, BindingResult result) {
        log.info("Se solicita crear una operación a la cuenta {}: {}", id, issuerRequest);
        if (result.hasErrors()) {
            log.error("La validación de la operacion fallo: {}", apiUtils.formatMessage(result));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        ResponseEntity<OperationResponse> responseEntity = operationService.createOperation(id, issuerRequest);
        log.info("Se retorna la operacion: {}", responseEntity);
        return responseEntity;
    }

}
