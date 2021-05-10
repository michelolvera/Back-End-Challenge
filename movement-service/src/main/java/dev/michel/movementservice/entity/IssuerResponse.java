package dev.michel.movementservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssuerResponse {

    @JsonProperty(value = "issuer_name")
    private String issuerName;
    private Integer total_shares;
    private Double share_price;
}
