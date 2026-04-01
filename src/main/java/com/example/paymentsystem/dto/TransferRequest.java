package com.example.paymentsystem.dto;

import java.math.BigDecimal;
import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
public class TransferRequest {
    @NotNull
    private Long receiverId;
    
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}