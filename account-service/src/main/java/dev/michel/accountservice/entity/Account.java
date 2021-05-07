package dev.michel.accountservice.entity;

import dev.michel.accountservice.model.Issuer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @Transient
    private List<Issuer> issuers;

}
