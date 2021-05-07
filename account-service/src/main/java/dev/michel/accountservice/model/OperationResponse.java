package dev.michel.accountservice.model;

import lombok.Data;

import java.util.List;

@Data
public class OperationResponse {
    private Balance current_balance;
    private List<String> business_errors;
}
