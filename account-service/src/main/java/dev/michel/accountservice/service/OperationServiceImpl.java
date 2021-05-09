package dev.michel.accountservice.service;

import dev.michel.accountservice.client.MovementClientCircuitBreaker;
import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.model.Balance;
import dev.michel.accountservice.model.Issuer;
import dev.michel.accountservice.model.OperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService{

    private final MovementClientCircuitBreaker movementClient;
    private final AccountService accountService;

    @Override
    public OperationResponse createOperation(Long id, Issuer issuer) {
        Account currentAccount = accountService.getAccount(id);
        if (currentAccount == null){
            return OperationResponse.builder()
                    .business_errors(Collections.singletonList("INEXISTENT_ACCOUNT"))
                    .current_balance(null)
                    .build();
        }
        ResponseEntity<List<Issuer>> currentIssuersResponseEntity = movementClient.createMovement(id, issuer);
        if (currentIssuersResponseEntity.getStatusCode() != HttpStatus.CREATED){
            return OperationResponse.builder()
                    .business_errors(Collections.singletonList("ERROR_GENERATING_TRANSACTION"))
                    .current_balance(getCurrentBalance(currentAccount))
                    .build();
        }
        currentAccount.setIssuers(currentIssuersResponseEntity.getBody());
        // TODO: 09/05/2021 Reducir saldo de la cuenta.
        return OperationResponse.builder()
                .business_errors(new ArrayList<>())
                .current_balance(getCurrentBalance(currentAccount))
                .build();
    }

    private Balance getCurrentBalance(Account account){
        return Balance.builder()
                .cash(account.getCash())
                .issuers(account.getIssuers())
                .build();
    }
}
