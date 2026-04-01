package com.example.paymentsystem;

import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.entity.Wallet;
import com.example.paymentsystem.repository.UserRepository;
import com.example.paymentsystem.repository.WalletRepository;
import com.example.paymentsystem.service.TransactionService;
import com.example.paymentsystem.service.WebhookService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    private TransactionService txnService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WalletRepository walletRepo;

    @MockBean
    private WebhookService webhook;

    @Test
    void transfer_shouldUpdateBalancesCorrectly() {

        User u1 = new User();
        u1.setEmail("userA@test.com");
        u1.setPassword("pass");
        userRepo.save(u1);

        User u2 = new User();
        u2.setEmail("userB@test.com");
        u2.setPassword("pass");
        userRepo.save(u2);

        Wallet w1 = new Wallet();
        w1.setUserId(u1.getId());
        w1.setBalance(new BigDecimal("1000"));
        walletRepo.save(w1);

        Wallet w2 = new Wallet();
        w2.setUserId(u2.getId());
        w2.setBalance(new BigDecimal("500"));
        walletRepo.save(w2);

        txnService.transfer(
                u1.getId(),
                u2.getId(),
                new BigDecimal("200"),
                "test-key-1"
        );

        Wallet updatedA = walletRepo.findByUserId(u1.getId()).orElseThrow();
        Wallet updatedB = walletRepo.findByUserId(u2.getId()).orElseThrow();

        assertEquals(0, updatedA.getBalance().compareTo(new BigDecimal("800")));
        assertEquals(0, updatedB.getBalance().compareTo(new BigDecimal("700")));
    }
}
