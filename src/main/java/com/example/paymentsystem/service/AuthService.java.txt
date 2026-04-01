package com.example.paymentsystem.service;

import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.entity.Wallet;
import com.example.paymentsystem.repository.UserRepository;
import com.example.paymentsystem.repository.WalletRepository;
import com.example.paymentsystem.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

@Autowired
private UserRepository userRepo;

@Autowired
private WalletRepository walletRepo;

@Autowired
private PasswordEncoder passwordEncoder;

@Autowired
private JwtUtil jwtUtil;

@Transactional
public String register(String email, String password) {

    if (userRepo.findByEmail(email) != null) {
        throw new RuntimeException("User already exists");
    }

    String hashed = passwordEncoder.encode(password);

    User user = new User();
    user.setEmail(email);
    user.setPassword(hashed);
    userRepo.save(user);

    Wallet wallet = new Wallet();
    wallet.setUserId(user.getId());
    walletRepo.save(wallet);

    return jwtUtil.generateToken(user.getId());
}

public String login(String email, String password) {

    User user = userRepo.findByEmail(email);

    if (user == null) {
        throw new RuntimeException("User not found");
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    return jwtUtil.generateToken(user.getId());
}
