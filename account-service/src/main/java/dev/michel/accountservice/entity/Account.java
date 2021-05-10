package dev.michel.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.michel.accountservice.model.IssuerResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
@ApiModel(description = "Clase que representa una cuenta, el único valor requerido para la creación es Cash")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID de la cuenta")
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "Crédito de la cuenta, con este se pueden realizar operaciones")
    private Double cash;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    @ApiModelProperty(notes = "Fecha en la que la cuenta fue creada")
    private Date createAt;

    @JsonIgnore
    @ApiModelProperty(notes = "Estado de la cuenta (CREATED | DELETED)")
    private String status;

    @Transient
    @ApiModelProperty(notes = "Lista de emisores de cuentas")
    private List<IssuerResponse> issuers;

}
