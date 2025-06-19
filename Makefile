.PHONY: help start stop compile test test-batch test-shell test-batch-shell enable-logging disable-logging clean logs all psql

# Default target
help:
	@echo "JDBC Test Makefile"
	@echo ""
	@echo "Available targets:"
	@echo "  help           Show this help message"
	@echo "  start          Start PostgreSQL database"
	@echo "  stop           Stop PostgreSQL database and remove volumes"
	@echo "  compile        Compile Java source code"
	@echo "  test           Run basic JDBC test (executeUpdate)"
	@echo "  test-batch     Run batch JDBC test (addBatch/executeBatch)"
	@echo "  test-shell     Run shell script test"
	@echo "  test-batch-shell Run batch shell script test"
	@echo "  enable-logging Enable PostgreSQL logging"
	@echo "  disable-logging Disable PostgreSQL logging"
	@echo "  logs           Show PostgreSQL logs"
	@echo "  clean          Stop database and clean up"
	@echo "  all            Run complete test suite"

# Database management
start:
	docker-compose up -d

stop:
	docker-compose down -v

# Build
compile:
	mvn compile

# Test targets
test:
	mvn exec:java -Dexec.mainClass="com.example.JdbcTest"

test-batch:
	mvn exec:java -Dexec.mainClass="com.example.JdbcBatchTest"

test-shell:
	./test.sh

test-batch-shell:
	./test_batch.sh

# Logging
enable-logging:
	./enable_logging.sh

disable-logging:
	./disable_logging.sh

logs:
	docker logs jdbc-test-postgres -f

# Utility targets
clean: stop

# Complete test workflow
all: start enable-logging compile test test-batch test-shell disable-logging stop
	@echo "All tests completed successfully!"

# Individual test workflows with logging
test-with-logging: start enable-logging compile test disable-logging stop
	@echo "Basic test completed!"

test-batch-with-logging: start enable-logging compile test-batch disable-logging stop
	@echo "Batch test completed!"

test-shell-with-logging: start enable-logging test-shell disable-logging stop
	@echo "Shell test completed!"


################ Other utilities

psql:
	PGPASSWORD=testpass psql -h localhost -p 5432 -U testuser -d testdb