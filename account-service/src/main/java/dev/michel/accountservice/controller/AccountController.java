package dev.michel.accountservice.controller;

import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.service.AccountService;
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
@RequestMapping("/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("OK");
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

    private String formatMessage(BindingResult result) {
        StringBuilder errorsText = new StringBuilder();
        for (FieldError error : result.getFieldErrors()){
            errorsText.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
        }
        return errorsText.substring(0, errorsText.length()-1);
    }

}
