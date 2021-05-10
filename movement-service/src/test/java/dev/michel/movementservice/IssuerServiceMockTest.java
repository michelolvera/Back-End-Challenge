package dev.michel.movementservice;

import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Clase que prueba la lógica de negocio de IssuerService
 */
@SpringBootTest
public class IssuerServiceMockTest extends SetupMockTest{
    /**
     * Cuando e hace una operación en un horario no hábil se retorna Exception "Closed Market"
     */
    @Test
    public void whenMakeOperationInCloseMarket_thenThrownCloseMarketException(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        issuerRequest.setTimestamp(1571345950L);
        Assertions.assertThatThrownBy(() -> issuerService.createIssuer(issuerRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Closed Market");
    }

    /**
     * Cuando e hace una operación con los mismos datos en menis de 5 minutos retorna Exception "Duplicate Operation"
     */
    @Test
    public void whenMakeDuplicateOperation_thenThrownDuplicateOperationException(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        Assertions.assertThatThrownBy(() -> issuerService.createIssuer(issuerRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Duplicate Operation");
    }

    /**
     * Cuando se realiza una operación sin acciones suficientes retorna excepcion "You don't have enough shares"
     */
    @Test
    public void whenMakeOperationWithoutEnoughShares_thenThrownEnoughSharesException(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        issuerRequest.setIssuerName("GOOG");
        issuerRequest.setOperation("SELL");
        Assertions.assertThatThrownBy(() -> issuerService.createIssuer(issuerRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("You don't have enough shares");
    }

    /**
     * Cuando se realiza una operación de compra correcta se retorna la cantidad de acciones anteriores + nuevas.
     */
    @Test
    public void whenMakeOperation_thenReturnUpdatedShares(){
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        issuerRequest.setTimestamp(1620489471L);
        List<IssuerResponse> issuerResponses = issuerService.createIssuer(issuerRequest);
        Assertions.assertThat(issuerResponses.get(0).getTotal_shares())
                .isEqualTo(200);
    }

    /**
     * Cuando se buscan las acciones por ID se retorna una lista.
     */
    @Test
    public void whenGetAllIssuersByAccountId_thenReturnValidIssuerList(){
        List<IssuerResponse> issuerResponses = issuerService.getAllIssuersByAccountId(1L);
        Assertions.assertThat(issuerResponses.size()).isEqualTo(1);
    }
}
