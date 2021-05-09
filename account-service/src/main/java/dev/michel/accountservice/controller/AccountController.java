package dev.michel.accountservice.controller;

import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.model.Issuer;
import dev.michel.accountservice.model.OperationResponse;
import dev.michel.accountservice.service.AccountService;
import dev.michel.accountservice.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<Account> status(@PathVariable("id") Long id) {
        Account account = accountService.getAccount(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account, BindingResult result){
        log.info("Creating New Account : {}", account);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Account accountCreate = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id, @Valid @RequestBody Account account, BindingResult result){
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        account.setId(id);
        Account accountDB = accountService.updateAccount(account);
        return accountDB == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(accountDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Account> deleteAccount(@PathVariable("id") Long id){
        Account accountDB = accountService.deleteAccount(id);
        if (accountDB == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountDB);
    }

    @PostMapping(value = "/{id}/orders")
    public ResponseEntity<OperationResponse> createIssuer(@PathVariable("id") Long id, @Valid @RequestBody Issuer issuer, BindingResult result){
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        OperationResponse operationResponse = operationService.createOperation(id, issuer);
        return ResponseEntity.ok(operationResponse);
    }

    private String formatMessage(BindingResult result) {
        StringBuilder errorsText = new StringBuilder();
        for (FieldError error : result.getFieldErrors()){
            errorsText.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
        }
        return errorsText.substring(0, errorsText.length()-1);
    }

}
