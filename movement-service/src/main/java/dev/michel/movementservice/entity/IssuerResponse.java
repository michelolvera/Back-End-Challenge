package dev.michel.movementservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class IssuerResponse {

    @JsonProperty(value = "issuer_name")
    private String issuerName;
    private Integer total_shares;
    private Double share_price;
}
