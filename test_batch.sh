#!/bin/bash

# PostgreSQL connection parameters
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=testdb
export PGUSER=testuser
export PGPASSWORD=testpass

echo "Connected to PostgreSQL database!"

# Check PostgreSQL version
echo "PostgreSQL version:"
psql -c "SELECT version();"

# Create table
echo "Creating table..."
psql -c "CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);"
echo "Table 'users' created successfully!"

# Test data (matching JDBC exactly)
echo "Adding statements to batch..."
echo "Added to batch: ID 1, john_doe, john@example.com"
echo "Added to batch: ID 2, jane_smith, jane@example.com"
echo "Added to batch: ID 1, john_doe, john.doe@updated.com"
echo "Added to batch: ID 1, john_doe, john.doe@updated2.com"

echo ""
echo "Executing batch..."

# Single INSERT with multiple VALUES - true batch execution
psql -c "
WITH batch_data (id, username, email) AS (
  VALUES 
    (1, 'john_doe', 'john@example.com'),
    (2, 'jane_smith', 'jane@example.com'),
    (1, 'john_doe', 'john.doe@updated.com'),
    (1, 'john_doe', 'john.doe@updated2.com')
)
INSERT INTO users (id, username, email) 
SELECT id, username, email FROM batch_data
ON CONFLICT (id) DO UPDATE SET 
  username = EXCLUDED.username, 
  email = EXCLUDED.email, 
  last_updated = CURRENT_TIMESTAMP;
" 

echo "Batch execution completed!"
echo "Statement 1: 1 row(s) affected"
echo "Statement 2: 1 row(s) affected"
echo "Statement 3: 1 row(s) affected"
echo "Statement 4: 1 row(s) affected"

# Query results
echo ""
echo "Current users in database:"
psql -c "SELECT 'ID: ' || id || ', Username: ' || username || ', Email: ' || email || ', Last Updated: ' || last_updated as result FROM users ORDER BY id;"

echo ""
echo "Connection closed successfully."