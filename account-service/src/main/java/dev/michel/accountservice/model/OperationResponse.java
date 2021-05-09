package dev.michel.accountservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OperationResponse {
    private Balance current_balance;
    private List<String> business_errors;
}
