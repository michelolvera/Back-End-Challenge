package dev.michel.movementservice.controller;

import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;
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
    public ResponseEntity<List<IssuerResponse>> getAllIssuersByAccountId(@PathVariable("accountId") Long accountId){
        return ResponseEntity.ok(issuerService.getAllIssuersByAccountId(accountId));
    }

    @PostMapping(value = "/{accountId}")
    public ResponseEntity<List<IssuerResponse>> createMovement(@PathVariable("accountId") Long accountId, @Valid @RequestBody IssuerRequest issuerRequest, BindingResult result){
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        issuerRequest.setAccountId(accountId);
        List<IssuerResponse> actualIssuerResponse = issuerService.createIssuer(issuerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualIssuerResponse);
    }

    private String formatMessage(BindingResult result) {
        StringBuilder errorsText = new StringBuilder();
        for (FieldError error : result.getFieldErrors()){
            errorsText.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
        }
        return errorsText.substring(0, errorsText.length()-1);
    }
}
