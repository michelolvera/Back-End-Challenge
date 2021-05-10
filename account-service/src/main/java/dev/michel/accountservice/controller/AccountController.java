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
    public ResponseEntity<Account> status(@PathVariable("id") Long id) {
        Account account = accountService.getAccount(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    }

    @PostMapping
    @ApiOperation("Crea y retorna una nueva cuenta, solo es necesario enviar la cantidad de crédito (cash)")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account, BindingResult result) {
        log.info("Creating New Account : {}", account);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        if (account.getCash() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cash: debe ser mayor que 0");
        }
        Account accountCreate = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreate);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation("Actualiza el saldo de una cuenta, sumando el valor enviado en el atributo cash")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id, @Valid @RequestBody Account account, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        account.setId(id);
        Account accountDB = accountService.updateAccount(account);
        return accountDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(accountDB);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Elimina una cuenta, siempre y cuando esta no cuente con saldo o acciones")
    public ResponseEntity<Account> deleteAccount(@PathVariable("id") Long id) {
        Account accountDB = accountService.deleteAccount(id);
        if (accountDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountDB);
    }

    @PostMapping(value = "/{id}/orders")
    @ApiOperation("Genera una operación de compra o venta en una cuenta")
    public ResponseEntity<OperationResponse> createIssuer(@PathVariable("id") Long id, @Valid @RequestBody IssuerRequest issuerRequest, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, apiUtils.formatMessage(result));
        }
        return operationService.createOperation(id, issuerRequest);
    }

}
