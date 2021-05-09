package dev.michel.movementservice.service;

import dev.michel.movementservice.entity.Issuer;
import dev.michel.movementservice.repository.IssuerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssuerServiceImpl implements IssuerService{

    private final IssuerRepository issuerRepository;

    @Override
    public List<Issuer> createIssuer(Issuer issuer) {
        issuer.setCreateAt(new Date());
        issuerRepository.save(issuer);
        return getAllIssuersByAccountId(issuer.getAccountId());
    }

    @Override
    public List<Issuer> getAllIssuersByAccountId(Long accountId) {
        return issuerRepository.findAllByAccountId(accountId);
    }
}
