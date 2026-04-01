package com.example.paymentsystem.service;

import com.example.paymentsystem.entity.Wallet;
import com.example.paymentsystem.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepo;

    public WalletService(WalletRepository walletRepo) {
        this.walletRepo = walletRepo;
    }

    @Transactional
    public void addMoney(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepo.findByUserId(userId).orElseThrow();
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepo.save(wallet);
    }

    public BigDecimal getBalance(Long userId) {
        return walletRepo.findByUserId(userId).orElseThrow().getBalance();
    }
}