package dev.michel.movementservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
public class IssuerRequest extends IssuerResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID del registro")
    private Long id;

    @ApiModelProperty(notes = "ID de la cuenta")
    private Long accountId;

    @NotNull
    @Transient
    @ApiModelProperty(notes = "Fecha y hora en formato EPOCH que representa cuando la operación tomara lugar")
    private Long timestamp;

    @NotNull
    @NotEmpty
    @JsonProperty(value = "issuer_name")
    @ApiModelProperty(notes = "Nombre del emisor de las acciones")
    private String issuerName;

    @NotNull
    @Positive
    @ApiModelProperty(notes = "Cantidad de acciones en la operación")
    private Integer total_shares;

    @NotNull
    @Positive
    @ApiModelProperty(notes = "Precio de cada acción al momento de la operación")
    private Double share_price;

    @NotNull
    @Pattern(regexp = "^(BUY|SELL|buy|sell)$")
    @Transient
    @ApiModelProperty(notes = "Tipo de operación (BUY | SELL)")
    private String operation;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(notes = "Fecha y hora en la que este registro fue creado")
    private Date createAt;
}
