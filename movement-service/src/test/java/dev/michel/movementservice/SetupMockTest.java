package dev.michel.movementservice;

import dev.michel.movementservice.entity.IssuerRegistry;
import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.repository.IssuerRegistryRepository;
import dev.michel.movementservice.repository.IssuerRepository;
import dev.michel.movementservice.service.IssuerService;
import dev.michel.movementservice.service.IssuerServiceImpl;
import dev.michel.movementservice.util.ApiUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

/**
 * Clase que configura el entorno de pruebas y de la cual las pruebas extienden
 */
public class SetupMockTest {
    IssuerService issuerService;

    @Mock
    private IssuerRepository issuerRepository;
    @Mock
    private IssuerRegistryRepository issuerRegistryRepository;
    private final ApiUtils apiUtils = new ApiUtils();

    /**
     * Método que aplica la configuración de un repositorio ficticio
     */
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        issuerService = new IssuerServiceImpl(issuerRepository, issuerRegistryRepository);
        IssuerRequest issuerRequest = getBasicIssuerRequest();
        IssuerRegistry issuerRegistry = apiUtils.issuerRequestToIssuerRegistryMapper(issuerRequest);

        Mockito.when(issuerRepository.findAllByAccountId(1L))
                .thenReturn(Collections.singletonList(issuerRequest));
        Mockito.when(issuerRepository.findByAccountIdAndIssuerName(1L, "AAPL"))
                .thenReturn(issuerRequest);
        Mockito.when(issuerRepository.save(issuerRequest))
                .thenReturn(issuerRequest);

        Mockito.when(issuerRegistryRepository.findByAccountIdAndIssuerNameAndTotalSharesAndSharePriceAndOperation(issuerRegistry.getAccountId(), issuerRegistry.getIssuerName(), issuerRegistry.getTotalShares(), issuerRegistry.getSharePrice(), issuerRegistry.getOperation()))
                .thenReturn(issuerRegistry);
        Mockito.when(issuerRegistryRepository.save(issuerRegistry))
                .thenReturn(issuerRegistry);
    }

    /**
     * Método que retorna una operación a realizar para pruebas
     * @return Operación a realizar con datos ficticios
     */
    IssuerRequest getBasicIssuerRequest(){
        IssuerRequest issuerRequest = new IssuerRequest();
        issuerRequest.setAccountId(1L);
        issuerRequest.setTimestamp(1571325625L);
        issuerRequest.setOperation("BUY");
        issuerRequest.setIssuerName("AAPL");
        issuerRequest.setTotal_shares(100);
        issuerRequest.setShare_price(10.0);
        return issuerRequest;
    }
}
