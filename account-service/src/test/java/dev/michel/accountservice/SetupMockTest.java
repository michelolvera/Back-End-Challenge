package dev.michel.accountservice;

import dev.michel.accountservice.client.MovementClientCircuitBreaker;
import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.repository.AccountRepository;
import dev.michel.accountservice.service.AccountService;
import dev.michel.accountservice.service.AccountServiceImpl;
import dev.michel.accountservice.service.OperationService;
import dev.michel.accountservice.service.OperationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

/**
 * Clase que configura el entorno de pruebas y de la cual las pruebas extienden
 */
public class SetupMockTest {
    OperationService operationService;
    AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Autowired
    private MovementClientCircuitBreaker movementClient;

    /**
     * Método que aplica la configuración de un repositorio ficticio
     */
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountRepository, movementClient);
        operationService = new OperationServiceImpl(movementClient, accountService);
        Account account = new Account();
        account.setId(1L);
        account.setCash(1000.0);
        account.setCreateAt(new Date());
        account.setStatus("CREATED");

        Mockito.when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        Mockito.when(accountRepository.save(account))
                .thenReturn(account);
    }
}
