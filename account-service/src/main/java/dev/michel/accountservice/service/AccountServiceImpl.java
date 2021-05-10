package dev.michel.accountservice.service;

import dev.michel.accountservice.client.MovementClientCircuitBreaker;
import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

/**
 * Implementación del servicio que permite realizar CRUD de cuentas
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MovementClientCircuitBreaker movementClient;

    /**
     * Implementación del método para obtener una cuenta
     *
     * @param id ID de la cuenta que realizará la operación
     * @return Account el objeto cuenta requerido
     */
    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account.getStatus().equalsIgnoreCase("DELETED"))
            return null;
        if (account != null)
            account.setIssuers(movementClient.getAllIssuersByAccountId(id).getBody());
        return account;
    }

    /**
     * Implementación del método que crea una nueva cuenta
     *
     * @param account El objeto cuenta con el saldo inicial
     * @return Account el objeto cuenta requerido
     */
    @Override
    public Account createAccount(Account account) {
        account.setStatus("CREATED");
        account.setCreateAt(new Date());
        Account accountDB = accountRepository.save(account);
        accountDB.setIssuers(movementClient.movementsFallback(account.getId(), null).getBody());
        return accountDB;
    }

    /**
     * Implementación del método que actualiza el saldo de una cuenta
     *
     * @param account El objeto cuenta con el saldo nuevo
     * @return Account el objeto cuenta actualizado
     */
    @Override
    public Account updateAccount(Account account) {
        Account accountDB = getAccount(account.getId());
        if (accountDB == null) {
            return null;
        }
        accountDB.setCash(accountDB.getCash() + account.getCash());
        accountDB = accountRepository.save(accountDB);
        return accountDB;
    }

    /**
     * Implementación del método que permite la eliminación de una cuenta
     *
     * @param id ID de la cuenta que realizará la operación
     * @return Account el objeto cuenta actualizado
     */
    @Override
    public Account deleteAccount(Long id) {
        Account accountDB = getAccount(id);
        if (accountDB == null) {
            return null;
        }
        if (accountDB.getCash() > 0 || !accountDB.getIssuers().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot delete an account with cash or shares");
        accountDB.setStatus("DELETED");
        return accountRepository.save(accountDB);
    }
}
