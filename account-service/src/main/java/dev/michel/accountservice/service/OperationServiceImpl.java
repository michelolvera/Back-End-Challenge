package dev.michel.accountservice.service;

import dev.michel.accountservice.client.MovementClientCircuitBreaker;
import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.model.Balance;
import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.IssuerResponse;
import dev.michel.accountservice.model.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService{

    private final MovementClientCircuitBreaker movementClient;
    private final AccountService accountService;

    @Override
    public ResponseEntity<OperationResponse> createOperation(Long id, IssuerRequest issuerRequest) {
        Account currentAccount = accountService.getAccount(id);
        if (currentAccount == null){
            return ResponseEntity.badRequest().body(operationResponseErrorBuilder(null, "INEXISTENT_ACCOUNT"));
        }
        // TODO: 09/05/2021 Close Market
        if(isClosedMarket(issuerRequest.getTimestamp()))
            return ResponseEntity.badRequest().body(operationResponseErrorBuilder(currentAccount, "CLOSED_MARKET"));
        Double operationValue = issuerRequest.getShare_price() * issuerRequest.getTotal_shares();
        switch (issuerRequest.getOperation().toUpperCase()){
            case "BUY":
                if (currentAccount.getCash() < operationValue)
                    return ResponseEntity.badRequest().body(operationResponseErrorBuilder(currentAccount, "NOT_ENOUGH_CASH"));
                operationValue = -operationValue;
                break;
            case "SELL":
                if (currentAccount.getIssuers().stream().noneMatch(currentIssuer -> currentIssuer.getIssuer_name().equalsIgnoreCase(issuerRequest.getIssuer_name()) && currentIssuer.getTotal_shares() >= issuerRequest.getTotal_shares()))
                    return ResponseEntity.badRequest().body(operationResponseErrorBuilder(currentAccount, "NOT_ENOUGH_SHARES"));
                break;
        }
        Account updatedAccount = updateAccountCash(currentAccount, operationValue);
        if(updatedAccount == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResponseErrorBuilder(currentAccount, "ERROR_GENERATING_TRANSACTION"));
        }
        ResponseEntity<List<IssuerResponse>> currentIssuersResponseEntity = movementClient.createMovement(id, issuerRequest);
        if (currentIssuersResponseEntity.getStatusCode() != HttpStatus.CREATED){
            updatedAccount = updateAccountCash(currentAccount, -operationValue);
            if(updatedAccount == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResponseErrorBuilder(currentAccount, "ERROR_UNDOING_OPERATION"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResponseErrorBuilder(updatedAccount, "ERROR_GENERATING_TRANSACTION"));
        }
        updatedAccount.setIssuers(currentIssuersResponseEntity.getBody());
        return ResponseEntity.ok(OperationResponse.builder()
                .business_errors(new ArrayList<>())
                .current_balance(getCurrentBalance(updatedAccount))
                .build());
    }

    private OperationResponse operationResponseErrorBuilder(Account account, String errorDescription){
        return OperationResponse.builder()
                .business_errors(Collections.singletonList(errorDescription))
                .current_balance(account == null ? null : getCurrentBalance(account))
                .build();
    }

    private Account updateAccountCash(Account account, Double operationValue){
        Account tempAccount = new Account();
        tempAccount.setId(account.getId());
        tempAccount.setCash(operationValue);
        return accountService.updateAccount(tempAccount);
    }

    private Balance getCurrentBalance(Account account){
        return Balance.builder()
                .cash(account.getCash())
                .issuers(account.getIssuers())
                .build();
    }

    private boolean isClosedMarket(Long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeStamp * 1000));
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        return hourOfDay < 6 || hourOfDay >= 15;
    }

}
