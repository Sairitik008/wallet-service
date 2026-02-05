# Wallet REST API

A high-performance wallet management system built with Java and Spring Boot, designed to handle concurrent transactions at scale (1000 RPS per wallet).

## Overview

This application provides a RESTful API for managing digital wallets with support for deposit and withdrawal operations. It's built with concurrency in mind to ensure data consistency under high load conditions.

## Features

- **Wallet Operations**: Deposit and withdraw funds from wallets
- **Balance Inquiry**: Retrieve current wallet balance
- **High Concurrency Support**: Handles 1000+ requests per second per wallet
- **Data Consistency**: Ensures no race conditions during concurrent operations
- **Database Migrations**: Managed with Liquibase
- **Dockerized**: Fully containerized application and database
- **Comprehensive Testing**: Full test coverage for all endpoints

## Tech Stack

- **Java**: 8-17
- **Spring Boot**: 3.x
- **Database**: PostgreSQL
- **Migration Tool**: Liquibase
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven/Gradle

## API Endpoints

### 1. Create Wallet Transaction

Perform a deposit or withdrawal operation on a wallet.

**Endpoint**: `POST /api/v1/wallet`

**Request Body**:
```json
{
  "walletId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "operationType": "DEPOSIT",
  "amount": 1000
}
```

**Operation Types**:
- `DEPOSIT`: Add funds to wallet
- `WITHDRAW`: Remove funds from wallet

**Success Response** (200 OK):
```json
{
  "walletId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "balance": 1000,
  "status": "SUCCESS"
}
```

**Error Responses**:

- `400 Bad Request`: Invalid JSON or missing required fields
- `404 Not Found`: Wallet does not exist
- `422 Unprocessable Entity`: Insufficient funds for withdrawal
- `500 Internal Server Error`: Should never occur under normal operation

### 2. Get Wallet Balance

Retrieve the current balance of a specific wallet.

**Endpoint**: `GET /api/v1/wallets/{WALLET_UUID}`

**Success Response** (200 OK):
```json
{
  "walletId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "balance": 5000
}
```

**Error Responses**:

- `400 Bad Request`: Invalid UUID format
- `404 Not Found`: Wallet does not exist

## Project Structure

```
wallet-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/wallet/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       └── WalletApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/changelog/
│   │           └── db.changelog-master.xml
│   └── test/
│       └── java/
│           └── com/example/wallet/
├── docker-compose.yml
├── Dockerfile
├── pom.xml (or build.gradle)
└── README.md
```

## Setup and Installation

### Prerequisites

- Docker (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)
- Git

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/wallet-api.git
   cd wallet-api
   ```

2. **Configure Environment Variables**

   Create a `.env` file in the project root (optional, uses defaults if not provided):
   
   ```env
   # Application Configuration
   SERVER_PORT=8080
   SPRING_PROFILES_ACTIVE=prod
   
   # Database Configuration
   POSTGRES_DB=walletdb
   POSTGRES_USER=walletuser
   POSTGRES_PASSWORD=walletpass
   POSTGRES_PORT=5432
   
   # Database Connection Pool
   HIKARI_MAXIMUM_POOL_SIZE=20
   HIKARI_MINIMUM_IDLE=5
   
   # Logging
   LOGGING_LEVEL_ROOT=INFO
   LOGGING_LEVEL_APP=DEBUG
   ```

3. **Build and Start the Application**
   ```bash
   docker-compose up --build
   ```

   For background execution:
   ```bash
   docker-compose up -d --build
   ```

4. **Verify the Application is Running**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

### Stopping the Application

```bash
docker-compose down
```

To remove volumes as well:
```bash
docker-compose down -v
```

## Configuration

### Application Configuration

Edit `src/main/resources/application.yml` or use environment variables to configure:

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:walletdb}
    username: ${DB_USERNAME:walletuser}
    password: ${DB_PASSWORD:walletpass}
    hikari:
      maximum-pool-size: ${HIKARI_MAXIMUM_POOL_SIZE:20}
      minimum-idle: ${HIKARI_MINIMUM_IDLE:5}
  
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml
    enabled: true

logging:
  level:
    root: ${LOGGING_LEVEL_ROOT:INFO}
    com.example.wallet: ${LOGGING_LEVEL_APP:DEBUG}
```

### Docker Compose Configuration

The `docker-compose.yml` file can be customized without rebuilding containers by modifying environment variables:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: ${POSTGRES_DB:-walletdb}
      POSTGRES_USER: ${POSTGRES_USER:-walletuser}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-walletpass}
    ports:
      - "${POSTGRES_PORT:-5432}:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

  wallet-api:
    build: .
    environment:
      DB_HOST: postgres
      DB_PORT: 5432
      DB_NAME: ${POSTGRES_DB:-walletdb}
      DB_USERNAME: ${POSTGRES_USER:-walletuser}
      DB_PASSWORD: ${POSTGRES_PASSWORD:-walletpass}
      SERVER_PORT: ${SERVER_PORT:-8080}
    ports:
      - "${SERVER_PORT:-8080}:8080"
    depends_on:
      - postgres

volumes:
  postgres-data:
```

## Database Migrations

Liquibase is used for database version control. Migration files are located in:
```
src/main/resources/db/changelog/
```

### Creating a New Migration

Add a new changeset to `db.changelog-master.xml`:

```xml
<changeSet id="create-wallet-table" author="developer">
    <createTable tableName="wallet">
        <column name="id" type="UUID">
            <constraints primaryKey="true" nullable="false"/>
        </column>
        <column name="balance" type="DECIMAL(19,2)">
            <constraints nullable="false"/>
        </column>
        <column name="version" type="BIGINT" defaultValue="0">
            <constraints nullable="false"/>
        </column>
        <column name="created_at" type="TIMESTAMP" defaultValueComputed="CURRENT_TIMESTAMP"/>
        <column name="updated_at" type="TIMESTAMP" defaultValueComputed="CURRENT_TIMESTAMP"/>
    </createTable>
</changeSet>
```

Migrations run automatically when the application starts.

## Concurrency Handling

This application handles high concurrency through:

1. **Optimistic Locking**: Using version fields to prevent lost updates
2. **Database Transactions**: ACID compliance for all wallet operations
3. **Connection Pooling**: HikariCP for efficient database connection management
4. **Pessimistic Locking**: SELECT FOR UPDATE when necessary for critical operations

Example implementation ensures that concurrent withdrawals from the same wallet never result in negative balances or inconsistent states.

## Testing

### Running Tests

**Using Docker**:
```bash
docker-compose run wallet-api ./mvnw test
```

**Locally** (requires Java and PostgreSQL):
```bash
./mvnw test
```

**For Gradle**:
```bash
./gradlew test
```

### Test Coverage

All endpoints are covered by integration tests:
- Wallet transaction operations (deposit/withdraw)
- Balance retrieval
- Error scenarios (invalid wallet, insufficient funds, malformed requests)
- Concurrent transaction handling

## Performance Considerations

- **Tested for 1000 RPS per wallet**: Application maintains data consistency under high load
- **No 50X errors**: Proper error handling ensures no unprocessed requests
- **Database indexing**: Optimized queries for fast balance lookups
- **Connection pooling**: Configured for optimal throughput

## Error Handling

The application provides clear error responses:

| Status Code | Scenario | Response |
|-------------|----------|----------|
| 400 | Invalid JSON format | `{"error": "Invalid request format"}` |
| 404 | Wallet not found | `{"error": "Wallet not found"}` |
| 422 | Insufficient funds | `{"error": "Insufficient funds"}` |
| 500 | Server error | `{"error": "Internal server error"}` |

## Development

### Local Development Setup

1. Start only the database:
   ```bash
   docker-compose up postgres
   ```

2. Run the application locally:
   ```bash
   ./mvnw spring-boot:run
   ```

### Building the Application

**Maven**:
```bash
./mvnw clean package
```

**Gradle**:
```bash
./gradlew clean build
```

## Troubleshooting

### Common Issues

**Port Already in Use**:
```bash
# Change the port in .env file
SERVER_PORT=8081
docker-compose up
```

**Database Connection Failed**:
- Ensure PostgreSQL container is running: `docker-compose ps`
- Check database logs: `docker-compose logs postgres`
- Verify credentials in `.env` file

**Application Won't Start**:
- Check application logs: `docker-compose logs wallet-api`
- Ensure database migrations completed successfully
- Verify Java version compatibility

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

Project Link: [https://github.com/yourusername/wallet-api](https://github.com/yourusername/wallet-api)

---

**Note**: This project was developed as a technical assessment task demonstrating proficiency in Java, Spring Boot, PostgreSQL, Docker, and handling concurrent operations at scale.
