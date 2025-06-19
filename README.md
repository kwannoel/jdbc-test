# JDBC Test

Simple JDBC test that connects to PostgreSQL and demonstrates table creation, upserts, and parameter binding.

## Quick Start

1. **Start PostgreSQL**
   ```bash
   docker-compose up -d
   ```

2. **Run the test**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.JdbcTest"
   ```

3. **Stop PostgreSQL**
   ```bash
   docker-compose down
   ```

## Requirements

- Docker
- Maven
- Java 11+