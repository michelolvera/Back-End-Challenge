package dev.michel.accountservice;

import dev.michel.accountservice.entity.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

/**
 * Clase que prueba la lógica de negocio de AccountService
 */
@SpringBootTest
public class AccountServiceMockTest extends SetupMockTest{

    /**
     * Cuando se busca una cuenta por id, se regresa una cuenta valida.
     */
    @Test
    public void whenFindById_thenReturnValidAccount(){
        Account accountDB = accountService.getAccount(1L);
        Assertions.assertThat(accountDB.getId()).isEqualTo(1L);
        Assertions.assertThat(accountDB.getIssuers()).isEqualTo(new ArrayList<>());
    }

    /**
     * Cuando se actualiza el crédito de una cuenta, se retorna la cuenta con el crédito actualizado.
     */
    @Test
    public void whenUpdateAccount_thenReturnUpdatedCash(){
        Account account = new Account();
        account.setId(1L);
        account.setCash(-1000.0);
        Account accountDB = accountService.updateAccount(account);
        Assertions.assertThat(accountDB.getCash()).isEqualTo(0.0);
    }

    /**
     * Cuando se elimina una cuenta, se retorna con es estado "DELETED"
     */
    @Test
    public void whenDeleteAccount_theReturnUpdatedStatus(){
        whenUpdateAccount_thenReturnUpdatedCash();
        Account accountDB = accountService.deleteAccount(1L);
        Assertions.assertThat(accountDB.getStatus()).isEqualTo("DELETED");
    }

}
