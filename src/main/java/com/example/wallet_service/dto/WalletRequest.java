package com.example.wallet_service.dto;

import com.example.wallet_service.enums.OperationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class WalletRequest {

    @NotNull
    public UUID walletId;

    @NotNull
    public OperationType operationType;

    @NotNull
    @Min(1)
    public Long amount;
}
