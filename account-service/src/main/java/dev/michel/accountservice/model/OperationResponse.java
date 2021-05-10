package dev.michel.accountservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(description = "Clase que representa la respuesta de una operación")
public class OperationResponse {
    @ApiModelProperty(notes = "Representa el balance de una cuenta")
    private Balance current_balance;
    @ApiModelProperty(notes = "Lista de errores de negocio que se presentaron al momento de realizar una operación")
    private List<String> business_errors;
}
