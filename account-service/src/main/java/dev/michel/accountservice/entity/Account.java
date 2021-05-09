package dev.michel.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.michel.accountservice.model.Issuer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    @NotNull
    private Double cash;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createAt;

    @JsonIgnore
    private String status;

    @Transient
    private List<Issuer> issuers;

}
