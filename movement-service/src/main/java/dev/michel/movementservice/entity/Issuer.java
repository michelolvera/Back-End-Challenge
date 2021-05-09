package dev.michel.movementservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table
public class Issuer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    @NotNull
    private Long timestamp;

    @NotNull
    @Pattern(regexp = "^(BUY|SELL)$")
    private String operation;

    @NotNull
    @NotEmpty
    private String issuer_name;

    @NotNull
    @Positive
    private Integer total_shares;

    @NotNull
    @Positive
    private Double share_price;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createAt;
}
