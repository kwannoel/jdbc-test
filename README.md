# JDBC Test

Simple JDBC test that connects to PostgreSQL and demonstrates table creation, upserts, and parameter binding.

## Quick Start

1. **Start PostgreSQL**
   ```bash
   docker-compose up -d
   ```

2. **Enable logging**
   ```bash
   ./enable_logging.sh
   ```

3. **Compile**
   ```bash
   mvn compile
   ```

4. **Run the test**

   Basic java version, uses PreparedStatement.executeUpdate()
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.JdbcTest"
   ```

   Batch version, uses addBatch/executeBatch
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.JdbcBatchTest"
   ```

   Shell script version, uses addBatch/executeBatch
   ```bash
   ./test.sh
   ```

5. **Disable logging**
   ```bash
   ./disable_logging.sh
   ```

6. **Stop PostgreSQL**
   ```bash
   docker-compose down -v
   ```

## Requirements

- Docker
- Maven
- Java 11+
- PostgreSQL client (`psql`) for shell script