package com.example.paymentsystem.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AuthRequest {
    
   private String email;
    
   private String password;

}