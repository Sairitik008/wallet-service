package com.example.wallet_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
public class Wallet {

    @Id
    private UUID id;

    private Long balance;
}
