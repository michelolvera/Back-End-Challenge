package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.IssuerRegistry;
import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;
import dev.michel.movementservice.repository.IssuerRegistryRepository;
import dev.michel.movementservice.repository.IssuerRepository;
import dev.michel.movementservice.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementación del servicio que permite la administración de acciones
 */
@Service
@RequiredArgsConstructor
public class IssuerServiceImpl implements IssuerService {

    private final IssuerRepository issuerRepository;
    private final IssuerRegistryRepository issuerRegistryRepository;
    private final ApiUtils apiUtils = new ApiUtils();

    /**
     * Implementación del método que permite crear una Operación en la cuenta de un usuario
     *
     * @param issuerRequest Operación a realizar
     * @return Lista de acciones de un usuario
     */
    @Override
    public List<IssuerResponse> createIssuer(IssuerRequest issuerRequest) {
        if (isClosedMarket(issuerRequest.getTimestamp()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Closed Market");
        IssuerRegistry lastSameOperation = issuerRegistryRepository.findByAccountIdAndIssuerNameAndTotalSharesAndSharePriceAndOperation(issuerRequest.getAccountId(), issuerRequest.getIssuerName(), issuerRequest.getTotal_shares(), issuerRequest.getShare_price(), issuerRequest.getOperation());
        if (lastSameOperation != null && Minutes.minutesBetween(new DateTime(lastSameOperation.getOperationMoment()), new DateTime(apiUtils.timestampToCalendar(issuerRequest.getTimestamp()))).isLessThan(Minutes.minutes(5)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate Operation");
        issuerRequest.setOperation(issuerRequest.getOperation().toUpperCase());
        issuerRequest.setIssuerName(issuerRequest.getIssuerName().toUpperCase());
        IssuerRequest currentIssuer = issuerRepository.findByAccountIdAndIssuerName(issuerRequest.getAccountId(), issuerRequest.getIssuerName());
        if (currentIssuer == null) {
            if (issuerRequest.getOperation().equalsIgnoreCase("SELL"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough shares");
            issuerRequest.setCreateAt(new Date());
            issuerRepository.save(issuerRequest);
        } else {
            switch (issuerRequest.getOperation()) {
                case "BUY":
                    currentIssuer.setTotal_shares(currentIssuer.getTotal_shares() + issuerRequest.getTotal_shares());
                    break;
                case "SELL":
                    if (currentIssuer.getTotal_shares() < issuerRequest.getTotal_shares())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough shares");
                    break;
            }
            currentIssuer.setShare_price(issuerRequest.getShare_price());
            currentIssuer.setOperation(issuerRequest.getOperation());
            currentIssuer.setTimestamp(issuerRequest.getTimestamp());
            if (currentIssuer.getTotal_shares() - issuerRequest.getTotal_shares() == 0)
                issuerRepository.delete(currentIssuer);
            else {
                currentIssuer.setTotal_shares(currentIssuer.getTotal_shares() - issuerRequest.getTotal_shares());
                issuerRepository.save(currentIssuer);
            }
        }
        issuerRegistryRepository.save(issuerRequestToIssuerRegistryMapper(issuerRequest));
        return getAllIssuersByAccountId(issuerRequest.getAccountId());
    }

    /**
     * Implementación del método que permite obtener todas las acciones de un usuario
     *
     * @param accountId ID de la cuenta
     * @return Lista de acciones de un usuario
     */
    @Override
    public List<IssuerResponse> getAllIssuersByAccountId(Long accountId) {
        return new ArrayList<>(issuerRepository.findAllByAccountId(accountId));
    }

    /**
     * Método que valida si una operación esta siendo realizada en horario hábil
     *
     * @param timeStamp Fecha y hora en formato EPOCH que representa cuando la operación tomara lugar
     * @return boolean TRUE: Hora inhábil, FALSE: Hora hábil
     */
    private boolean isClosedMarket(Long timeStamp) {
        int hourOfDay = apiUtils.timestampToCalendar(timeStamp).get(Calendar.HOUR_OF_DAY);
        return hourOfDay < 6 || hourOfDay >= 15;
    }

    /**
     * Método que convierte un objeto issuerRequest a IssuerRegistry
     *
     * @param issuerRequest El objeto issuerRequest a convertir
     * @return El objeto IssuerRegistry con datos
     */
    private IssuerRegistry issuerRequestToIssuerRegistryMapper(IssuerRequest issuerRequest) {
        IssuerRegistry issuerRegistry = new IssuerRegistry();
        issuerRegistry.setAccountId(issuerRequest.getAccountId());
        issuerRegistry.setOperationMoment(apiUtils.timestampToCalendar(issuerRequest.getTimestamp()).getTime());
        issuerRegistry.setIssuerName(issuerRequest.getIssuerName());
        issuerRegistry.setTotalShares(issuerRequest.getTotal_shares());
        issuerRegistry.setSharePrice(issuerRequest.getShare_price());
        issuerRegistry.setOperation(issuerRequest.getOperation());
        return issuerRegistry;
    }
}
