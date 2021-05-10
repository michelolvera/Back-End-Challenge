package dev.michel.movementservice.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class IssuerRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID del registro")
    private Long id;

    @ApiModelProperty(notes = "ID de la cuenta")
    private Long accountId;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(notes = "Fecha y hora en que representa cuando la operación tomara lugar")
    private Date operationMoment;
    @ApiModelProperty(notes = "Nombre del emisor de las acciones")
    private String issuerName;
    @ApiModelProperty(notes = "Cantidad de acciones en la operación")
    private Integer totalShares;
    @ApiModelProperty(notes = "Precio de cada acción al momento de la operación")
    private Double sharePrice;
    @ApiModelProperty(notes = "Tipo de operación (BUY | SELL)")
    private String operation;
}
