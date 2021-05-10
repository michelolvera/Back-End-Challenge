package dev.michel.accountservice;

import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.OperationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@SpringBootTest
public class OperationServiceMockTest extends SetupMockTest {

    @Test
    public void whenMakeOperationToInexistentAccount_thenReturnInexistentAccountError(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        ResponseEntity<OperationResponse> responseEntity = operationService.createOperation(2L, issuerRequest);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getBusiness_errors().get(0)).isEqualTo("INEXISTENT_ACCOUNT");
    }

    @Test
    public void whenMakeOperationInCloseMarket_thenReturnCloseMarketError(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        issuerRequest.setTimestamp(1571345950L);
        ResponseEntity<OperationResponse> responseEntity = operationService.createOperation(1L, issuerRequest);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getBusiness_errors().get(0)).isEqualTo("CLOSED_MARKET");
        Assertions.assertThat(responseEntity.getBody().getCurrent_balance().getCash()).isEqualTo(1000);
    }

    @Test
    public void whenMakeBuyOperationWithoutEnoughCash_thenReturnEnoughCashError(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        issuerRequest.setTotal_shares(101);
        ResponseEntity<OperationResponse> responseEntity = operationService.createOperation(1L, issuerRequest);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getBusiness_errors().get(0)).isEqualTo("NOT_ENOUGH_CASH");
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getCurrent_balance().getCash()).isEqualTo(1000.0);
    }

    @Test
    public void whenMakeSellOperationWithoutEnoughShares_thenReturnEnoughSharesError(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        issuerRequest.setOperation("SELL");
        ResponseEntity<OperationResponse> responseEntity = operationService.createOperation(1L, issuerRequest);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getBusiness_errors().get(0)).isEqualTo("NOT_ENOUGH_SHARES");
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).getCurrent_balance().getCash()).isEqualTo(1000.0);
    }

    private IssuerRequest getBasicIssuerRequest(){
        IssuerRequest issuerRequest = new IssuerRequest();
        issuerRequest.setTimestamp(1571325625L);
        issuerRequest.setOperation("BUY");
        issuerRequest.setIssuer_name("AAPL");
        issuerRequest.setTotal_shares(100);
        issuerRequest.setShare_price(10.0);
        return issuerRequest;
    }
}
