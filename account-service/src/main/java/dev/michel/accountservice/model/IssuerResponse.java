package dev.michel.accountservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class IssuerResponse {
    @NotNull
    @NotEmpty
    private String issuer_name;

    @NotNull
    @Positive
    private Integer total_shares;

    @NotNull
    @Positive
    private Double share_price;
}
