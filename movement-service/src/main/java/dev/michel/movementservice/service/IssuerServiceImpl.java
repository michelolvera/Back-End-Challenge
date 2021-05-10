package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.IssuerRegistry;
import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;
import dev.michel.movementservice.repository.IssuerRegistryRepository;
import dev.michel.movementservice.repository.IssuerRepository;
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

@Service
@RequiredArgsConstructor
public class IssuerServiceImpl implements IssuerService{

    private final IssuerRepository issuerRepository;
    private final IssuerRegistryRepository issuerRegistryRepository;

    @Override
    public List<IssuerResponse> createIssuer(IssuerRequest issuerRequest) {
        if (isClosedMarket(issuerRequest.getTimestamp()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Closed Market");
        IssuerRegistry lastSameOperation = issuerRegistryRepository.findByAccountIdAndIssuerNameAndTotalSharesAndSharePriceAndOperation(issuerRequest.getAccountId(), issuerRequest.getIssuerName(), issuerRequest.getTotal_shares(), issuerRequest.getShare_price(), issuerRequest.getOperation());
        if (lastSameOperation != null && Minutes.minutesBetween(new DateTime(lastSameOperation.getOperationMoment()), new DateTime(timestampToCalendar(issuerRequest.getTimestamp()))).isLessThan(Minutes.minutes(5)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate Operation");
        issuerRequest.setOperation(issuerRequest.getOperation().toUpperCase());
        issuerRequest.setIssuerName(issuerRequest.getIssuerName().toUpperCase());
        IssuerRequest currentIssuer = issuerRepository.findByAccountIdAndIssuerName(issuerRequest.getAccountId(), issuerRequest.getIssuerName());
        if (currentIssuer == null){
            if (issuerRequest.getOperation().equalsIgnoreCase("SELL"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough shares");
            issuerRequest.setCreateAt(new Date());
            issuerRepository.save(issuerRequest);
        }else{
            switch (issuerRequest.getOperation()){
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
        issuerRegistryRepository.save(issuerRequestToIssuerRegistry(issuerRequest));
        return getAllIssuersByAccountId(issuerRequest.getAccountId());
    }

    @Override
    public List<IssuerResponse> getAllIssuersByAccountId(Long accountId) {
        return new ArrayList<>(issuerRepository.findAllByAccountId(accountId));
    }

    private boolean isClosedMarket(Long timeStamp){
        int hourOfDay = timestampToCalendar(timeStamp).get(Calendar.HOUR_OF_DAY);
        return hourOfDay < 6 || hourOfDay >= 15;
    }

    private Calendar timestampToCalendar(Long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeStamp * 1000));
        return calendar;
    }

    private IssuerRegistry issuerRequestToIssuerRegistry(IssuerRequest issuerRequest){
        IssuerRegistry issuerRegistry = new IssuerRegistry();
        issuerRegistry.setAccountId(issuerRequest.getAccountId());
        issuerRegistry.setOperationMoment(timestampToCalendar(issuerRequest.getTimestamp()).getTime());
        issuerRegistry.setIssuerName(issuerRequest.getIssuerName());
        issuerRegistry.setTotalShares(issuerRequest.getTotal_shares());
        issuerRegistry.setSharePrice(issuerRequest.getShare_price());
        issuerRegistry.setOperation(issuerRequest.getOperation());
        return issuerRegistry;
    }
}
