package dev.michel.accountservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "Clase que representa la solicitud de una operación")
public class IssuerRequest extends IssuerResponse {
    @ApiModelProperty(notes = "Fecha y hora en formato EPOCH que representa cuando la operación tomara lugar")
    @NotNull
    private Long timestamp;

    @ApiModelProperty(notes = "Tipo de operación (BUY | SELL)")
    @NotNull
    @Pattern(regexp = "^(BUY|SELL|buy|sell)$")
    private String operation;
}
