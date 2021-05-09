package dev.michel.movementservice.controller;

import dev.michel.movementservice.entity.Issuer;
import dev.michel.movementservice.service.IssuerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<List<Issuer>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId){
        return ResponseEntity.ok(issuerService.getAllIssuersByAccountId(accountId));
    }

    @PostMapping(value = "/{accountId}")
    public ResponseEntity<List<Issuer>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody Issuer issuer, BindingResult result){
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        issuer.setAccountId(accountId);
        List<Issuer> actualIssuers = issuerService.createIssuer(issuer);
        if (actualIssuers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The transaction could not be created correctly, your balance was not affected.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(actualIssuers);
    }

    private String formatMessage(BindingResult result) {
        StringBuilder errorsText = new StringBuilder();
        for (FieldError error : result.getFieldErrors()){
            errorsText.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
        }
        return errorsText.substring(0, errorsText.length()-1);
    }
}
