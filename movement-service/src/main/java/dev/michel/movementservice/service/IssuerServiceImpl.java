package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.IssuerRequest;
import dev.michel.movementservice.entity.IssuerResponse;
import dev.michel.movementservice.repository.IssuerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssuerServiceImpl implements IssuerService{

    private final IssuerRepository issuerRepository;

    @Override
    public List<IssuerResponse> createIssuer(IssuerRequest issuerRequest) {
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
        return getAllIssuersByAccountId(issuerRequest.getAccountId());
    }

    @Override
    public List<IssuerResponse> getAllIssuersByAccountId(Long accountId) {
        return new ArrayList<>(issuerRepository.findAllByAccountId(accountId));
    }
}
