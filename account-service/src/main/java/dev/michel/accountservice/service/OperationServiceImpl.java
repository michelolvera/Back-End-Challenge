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

/**
 * Implementación de la interfaz OperationService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final MovementClientCircuitBreaker movementClient;
    private final AccountService accountService;

    /**
     * Implementación del método que realiza todas las validaciones de negocio al realizar una operación
     *
     * @param id            ID de la cuenta que realizará la operación
     * @param issuerRequest Datos de la operación a realizar
     * @return ResponseEntity<OperationResponse> con los resultados de la operación y el código de estado.
     */
    @Override
    public ResponseEntity<OperationResponse> createOperation(Long id, IssuerRequest issuerRequest) {
        Account currentAccount = accountService.getAccount(id);
        if (currentAccount == null) {
            return ResponseEntity.badRequest().body(operationResponseErrorBuilder(null, "INEXISTENT_ACCOUNT"));
        }
        if (isClosedMarket(issuerRequest.getTimestamp()))
            return ResponseEntity.badRequest().body(operationResponseErrorBuilder(currentAccount, "CLOSED_MARKET"));
        Double operationValue = issuerRequest.getShare_price() * issuerRequest.getTotal_shares();
        switch (issuerRequest.getOperation().toUpperCase()) {
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
        if (updatedAccount == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResponseErrorBuilder(currentAccount, "ERROR_GENERATING_TRANSACTION"));
        }
        ResponseEntity<List<IssuerResponse>> currentIssuersResponseEntity = movementClient.createMovement(id, issuerRequest);
        if (currentIssuersResponseEntity.getStatusCode() != HttpStatus.CREATED) {
            updatedAccount = updateAccountCash(currentAccount, -operationValue);
            if (updatedAccount == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResponseErrorBuilder(currentAccount, "ERROR_UNDOING_TRANSACTION"));
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(OperationResponse.builder()
                    .business_errors(Arrays.asList("ERROR_GENERATING_TRANSACTION", "HAS_IT_BEEN_5_MINUTES_SINCE_LAST_TRANSACTION?"))
                    .current_balance(getCurrentBalance(currentAccount))
                    .build());
        }
        updatedAccount.setIssuers(currentIssuersResponseEntity.getBody());
        return ResponseEntity.ok(OperationResponse.builder()
                .business_errors(new ArrayList<>())
                .current_balance(getCurrentBalance(updatedAccount))
                .build());
    }

    /**
     * Método que genera la respuesta que obtendrá el cliente en caso de obtener un error
     *
     * @param account          Cuenta que intenta realizar el movimiento
     * @param errorDescription Descripción del error presentado
     * @return OperationResponse Con los datos del cliente y el error
     */
    private OperationResponse operationResponseErrorBuilder(Account account, String errorDescription) {
        return OperationResponse.builder()
                .business_errors(Collections.singletonList(errorDescription))
                .current_balance(account == null ? null : getCurrentBalance(account))
                .build();
    }

    /**
     * Método que actualiza el crédito de una cuenta
     *
     * @param account        Cuenta que intenta realizar el movimiento
     * @param operationValue Valor monetario de la operación
     * @return Account Cuenta del cliente actualizada
     */
    private Account updateAccountCash(Account account, Double operationValue) {
        Account tempAccount = new Account();
        tempAccount.setId(account.getId());
        tempAccount.setCash(operationValue);
        return accountService.updateAccount(tempAccount);
    }

    /**
     * Método que genera un balance a partir de una cuenta
     *
     * @param account Cuenta que intenta realizar el movimiento
     * @return Balance con los datos de la cuenta
     */
    private Balance getCurrentBalance(Account account) {
        return Balance.builder()
                .cash(account.getCash())
                .issuers(account.getIssuers())
                .build();
    }

    /**
     * Método que valida si una operación esta siendo realizada en horario hábil
     *
     * @param timeStamp Fecha y hora en formato EPOCH que representa cuando la operación tomara lugar
     * @return boolean TRUE: Hora inhábil, FALSE: Hora hábil
     */
    private boolean isClosedMarket(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        calendar.setTime(new Date(timeStamp * 1000));
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        return hourOfDay < 6 || hourOfDay >= 15;
    }

}
