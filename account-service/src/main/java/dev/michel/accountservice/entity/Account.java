package dev.michel.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.michel.accountservice.model.IssuerRequest;
import dev.michel.accountservice.model.IssuerResponse;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double cash;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createAt;

    @JsonIgnore
    private String status;

    @Transient
    private List<IssuerResponse> issuers;

}
