package com.example.paymentsystem.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class TransferRequest {

    @NotNull
    private Long receiverId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    // Getter for receiverId
    public Long getReceiverId() {
        return receiverId;
    }

    // Setter for receiverId
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    // Getter for amount
    public BigDecimal getAmount() {
        return amount;
    }

    // Setter for amount
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}