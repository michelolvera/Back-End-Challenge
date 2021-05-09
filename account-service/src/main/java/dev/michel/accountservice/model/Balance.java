package dev.michel.accountservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Balance {
    private Double cash;
    private List<IssuerResponse> issuers;
}
