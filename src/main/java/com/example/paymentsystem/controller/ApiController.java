package com.example.paymentsystem.controller;

import com.example.paymentsystem.dto.*;
import com.example.paymentsystem.service.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final AuthService auth;
    private final WalletService wallet;
    private final TransactionService txn;

    public ApiController(AuthService auth,
                         WalletService wallet,
                         TransactionService txn) {
        this.auth = auth;
        this.wallet = wallet;
        this.txn = txn;
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest req) {
        auth.register(req.getEmail(), req.getPassword());
        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest req) {
        return auth.login(req.getEmail(), req.getPassword());
    }

   @PostMapping("/add")
   public void add(HttpServletRequest request,
   @RequestParam BigDecimal amount) {

   Long userId = (Long) request.getAttribute("userId");

   if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BadRequestException("Invalid amount");
   }

   wallet.addMoney(userId, amount);
 }


   @PostMapping("/transfer")
   public ResponseEntity<?> transfer(HttpServletRequest request,
                                  @valid
                                  @RequestBody TransferRequest req,
                                  @RequestHeader("Idempotency-Key") String referenceId) {

    Long senderId = (Long) request.getAttribute("userId");

    if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
       throw new BadRequestException("Invalid amount");
    }
    
    if (senderId.equalsreq.getReceiverId(){
        throw new BadRequestException("Cannot transfer to self");
    }

    txn.transfer(senderId,
                 req.getReceiverId(),
                 req.getAmount(),
                 referenceId);

    return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "referenceId", referenceId
    ));
}

 @GetMapping("/balance")
public BigDecimal balance(HttpServletRequest request) {

    Long userId = (Long) request.getAttribute("userId");

    return wallet.getBalance(userId);
}

    @GetMapping("/transactions/{userId}")
    public Object history(@PathVariable Long userId) {
        return txn.history(userId);
    }
}
