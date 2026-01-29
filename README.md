# Wallet Service – Spring Boot + PostgreSQL + Docker

This project is a backend wallet transaction service developed using **Java Spring Boot**, **PostgreSQL**, **Liquibase**, and **Docker**.  
It provides REST APIs to perform deposit and withdrawal operations and to retrieve wallet balance.  
The system is concurrency-safe and database-driven.

---

## Features

- REST API for wallet transactions
- Deposit and Withdraw operations
- Retrieve wallet balance
- PostgreSQL database
- Liquibase database migrations
- Concurrency-safe updates using pessimistic locking
- Global exception handling
- Dockerized application and database
- Ready for production-like deployment

---

## Tech Stack

- Java 17  
- Spring Boot  
- Spring Data JPA  
- PostgreSQL  
- Liquibase  
- Docker & Docker Compose  
- Maven  

---

## API Endpoints

### 1. Update Wallet Balance

**POST**
/api/v1/wallet

**Request Body**
```json
{
  "walletId": "00000000-0000-0000-0000-000000000001",
  "operationType": "DEPOSIT",
  "amount": 1000
}


2. Get Wallet Balance

/api/v1/wallets/{walletId}


#Docker
Running the Project (Docker)
Prerequisites

Docker Desktop installed

Docker Compose available

Java 17 (only required if building jar manually)
mvn clean package

#Start Docker Application
docker compose up --build

#This starts :
Spring Boot application on port 8080
PostgreSQL database on port 5432

Step 3: Insert Initial Wallet Record

docker exec -it wallet-service-db-1 psql -U walletuser walletdb
INSERT INTO wallet(id, balance) VALUES ('00000000-0000-0000-0000-000000000001', 0);

