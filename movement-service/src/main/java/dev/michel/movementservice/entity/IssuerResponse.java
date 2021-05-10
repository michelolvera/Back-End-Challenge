package dev.michel.movementservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IssuerResponse {

    @ApiModelProperty(notes = "Nombre del emisor de las acciones")
    @JsonProperty(value = "issuer_name")
    private String issuerName;
    @ApiModelProperty(notes = "Cantidad de acciones en la operación")
    private Integer total_shares;
    @ApiModelProperty(notes = "Precio de cada acción al momento de la operación")
    private Double share_price;
}
