package dev.michel.accountservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class IssuerRequest extends IssuerResponse{
    @NotNull
    private Long timestamp;

    @NotNull
    @Pattern(regexp = "^(BUY|SELL|buy|sell)$")
    private String operation;
}
