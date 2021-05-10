package dev.michel.movementservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class IssuerRequest extends IssuerResponse{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    @NotNull
    @Transient
    private Long timestamp;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "issuer_name")
    private String issuerName;

    @NotNull
    @Positive
    private Integer total_shares;

    @NotNull
    @Positive
    private Double share_price;

    @NotNull
    @Pattern(regexp = "^(BUY|SELL|buy|sell)$")
    @Transient
    private String operation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
}
