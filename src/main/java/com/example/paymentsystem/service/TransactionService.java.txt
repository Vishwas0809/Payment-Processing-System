package com.example.paymentsystem.service;

import com.example.paymentsystem.entity.*;
import com.example.paymentsystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final WalletRepository walletRepo;
    private final TransactionRepository txnRepo;
    private final WebhookService webhook;

    public TransactionService(WalletRepository walletRepo,
                              TransactionRepository txnRepo,
                              WebhookService webhook) {
        this.walletRepo = walletRepo;
        this.txnRepo = txnRepo;
        this.webhook = webhook;
    }

    public List<Transaction> history(Long userId) {
        return txnRepo.findBySenderIdOrReceiverId(userId, userId);
    }

    @Transactional
    public void transfer(Long senderId,
                     Long receiverId,
                     BigDecimal amount,
                     String referenceId) {

    if (txnRepo.existsByReferenceId(referenceId)) {
    return;
    }

    Transaction tx = new Transaction();
    tx.setReferenceId(referenceId);
    tx.setSenderId(senderId);
    tx.setReceiverId(receiverId);
    tx.setAmount(amount);
    tx.setStatus("PENDING");
    txnRepo.save(tx); 
    
    Wallet sender = walletRepo.findByUserIdForUpdate(senderId);
    Wallet receiver = walletRepo.findByUserIdForUpdate(receiverId);
    if (sender.getBalance().compareTo(amount) < 0) {
        tx.setStatus("FAILED");
        throw new RuntimeException("Insufficient balance");
    }
    sender.setBalance(sender.getBalance().subtract(amount));
    receiver.setBalance(receiver.getBalance().add(amount));
    tx.setStatus("SUCCESS");
    txnRepo.save(tx);
    webhook.send(senderId, receiverId, amount.toString());
}
