package com.example.wallet_service.service;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.enums.OperationType;
import com.example.wallet_service.exception.InsufficientFundsException;
import com.example.wallet_service.exception.WalletNotFoundException;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repo;

    // Uses pessimistic lock for safe concurrent updates
    @Transactional
    public Long process(WalletRequest req) {

        Wallet wallet = repo.findByIdForUpdate(req.walletId)
                .orElseThrow(WalletNotFoundException::new);

        if (req.operationType == OperationType.WITHDRAW) {
            if (wallet.getBalance() < req.amount)
                throw new InsufficientFundsException();
            wallet.setBalance(wallet.getBalance() - req.amount);
        } else {
            wallet.setBalance(wallet.getBalance() + req.amount);
        }

        return wallet.getBalance();
    }

    // Normal read — no locking needed
    @Transactional(readOnly = true)
    public Long getBalance(UUID id) {
        return repo.findById(id)
                .orElseThrow(WalletNotFoundException::new)
                .getBalance();
    }
}
