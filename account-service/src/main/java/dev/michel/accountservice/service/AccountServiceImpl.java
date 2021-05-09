package dev.michel.accountservice.service;

import dev.michel.accountservice.client.MovementClient;
import dev.michel.accountservice.client.MovementClientCircuitBreaker;
import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final MovementClientCircuitBreaker movementClient;

    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null)
            account.setIssuers(movementClient.getAllIssuersByAccountId(id).getBody());
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account createAccount(Account account) {
        account.setStatus("CREATED");
        account.setCreateAt(new Date());
        Account accountDB = accountRepository.save(account);
        if (accountDB != null)
            accountDB.setIssuers(movementClient.movementsFallback(account.getId(), null).getBody());
        return accountDB;
    }

    @Override
    public Account updateAccount(Account account) {
        Account accountDB = getAccount(account.getId());
        if (accountDB == null){
            return null;
        }
        accountDB.setCash(accountDB.getCash() + account.getCash());
        accountDB = accountRepository.save(accountDB);
        return accountDB;
    }

    @Override
    public Account deleteAccount(Long id) {
        Account accountDB = getAccount(id);
        if (accountDB == null){
            return null;
        }
        accountDB.setStatus("DELETED");
        accountDB = accountRepository.save(accountDB);
        return accountDB;
    }
}
