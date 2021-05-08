package dev.michel.accountservice.service;

import dev.michel.accountservice.entity.Account;
import dev.michel.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account createAccount(Account account) {
        account.setStatus("CREATED");
        account.setCreateAt(new Date());
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account) {
        Account accountDB = getAccount(account.getId());
        if (accountDB == null){
            return null;
        }
        accountDB.setCash(accountDB.getCash() + account.getCash());
        return accountRepository.save(accountDB);
    }

    @Override
    public Account deleteAccount(Long id) {
        Account accountDB = getAccount(id);
        if (accountDB == null){
            return null;
        }
        accountDB.setStatus("DELETED");
        return accountRepository.save(accountDB);
    }
}
