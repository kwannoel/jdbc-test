# JDBC Test

Simple JDBC test that connects to PostgreSQL and demonstrates table creation, upserts, and parameter binding.

## Quick Start

1. **Start PostgreSQL**
   ```bash
   docker-compose up -d
   ```

2. **Run the test**
   ```bash
   ./enable_logging.sh
   mvn compile
   mvn exec:java -Dexec.mainClass="com.example.JdbcTest"
   ./disable_logging.sh
   ```

   **Or run the shell script version:**
   ```bash
   ./test.sh
   ```

3. **Stop PostgreSQL**
   ```bash
   docker-compose down
   ```

## Requirements

- Docker
- Maven
- Java 11+
- PostgreSQL client (`psql`) for shell script