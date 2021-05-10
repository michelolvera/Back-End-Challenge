package dev.michel.accountservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@ApiModel(description = "Clase que representa el estado de cuenta de acciones de un emisor")
public class IssuerResponse {
    @ApiModelProperty(notes = "Nombre del emisor de las acciones")
    @NotNull
    @NotEmpty
    private String issuer_name;

    @ApiModelProperty(notes = "Cantidad de acciones en la operación")
    @NotNull
    @Positive
    private Integer total_shares;

    @ApiModelProperty(notes = "Precio de cada acción al momento de la operación")
    @NotNull
    @Positive
    private Double share_price;
}
