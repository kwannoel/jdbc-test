# JDBC Test

Simple JDBC test that connects to PostgreSQL and demonstrates table creation, upserts, and parameter binding.

## Quick Start

```bash
make help                    # Show all available commands
make start                   # Start PostgreSQL
make enable-logging          # Enable logging
make compile                 # Compile
make test                    # Run basic test
make test-batch              # Run batch test
make test-shell              # Run shell script test
make test-batch-shell        # Run batch shell script test
make test-with-logging       # Run basic test with full workflow
make test-batch-with-logging # Run batch test with full workflow  
make all                     # Run complete test suite
```

## Requirements

- Docker
- Maven
- Java 11+
- PostgreSQL client (`psql`) for shell script