package dev.michel.accountservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(description = "Clase que representa el balance de una cuenta")
public class Balance {
    @ApiModelProperty(notes = "Crédito de la cuenta, con este se pueden realizar operaciones")
    private Double cash;
    @ApiModelProperty(notes = "Lista de emisores de cuentas")
    private List<IssuerResponse> issuers;
}
