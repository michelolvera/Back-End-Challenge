package dev.michel.accountservice.model;

import lombok.Data;

@Data
public class Issuer {
    private String issuer_name;
    private Integer total_shares;
    private Double share_price;
}
