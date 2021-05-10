package dev.michel.movementservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class IssuerRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date operationMoment;

    private String issuerName;
    private Integer totalShares;
    private Double sharePrice;
    private String operation;
}
