package dev.michel.accountservice.service;

import dev.michel.accountservice.entity.Account;

public interface AccountService {

    Account getAccount(Long id);
    Account createAccount(Account account);
    Account updateAccount(Account account);
    Account deleteAccount(Long id);

}
