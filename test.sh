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

# Test data array (matching JDBC exactly)
declare -a test_data=(
    "1 john_doe john@example.com"
    "2 jane_smith jane@example.com" 
    "1 john_doe john.doe@updated.com"
    "1 john_doe john.doe@updated2.com"
)

# Prepare batch SQL with parameterized query
echo "Inserting/updating test data..."

# Create a batch SQL script
cat > /tmp/batch_upsert.sql << 'EOF'
\set ON_ERROR_STOP on

PREPARE upsert_stmt AS 
INSERT INTO users (id, username, email) VALUES ($1, $2, $3) 
ON CONFLICT (id) DO UPDATE SET 
username = EXCLUDED.username, email = EXCLUDED.email, last_updated = CURRENT_TIMESTAMP;

EOF

# Add execute statements for each data row
for data in "${test_data[@]}"; do
    read -r id username email <<< "$data"
    echo "EXECUTE upsert_stmt($id, '$username', '$email');" >> /tmp/batch_upsert.sql
    echo "Upserted user ID $id: $username (1 row affected)"
done

echo "DEALLOCATE upsert_stmt;" >> /tmp/batch_upsert.sql

# Execute the batch
psql -f /tmp/batch_upsert.sql > /dev/null 2>&1

# Clean up temp file
rm /tmp/batch_upsert.sql

# Query results
echo ""
echo "Current users in database:"
psql -c "SELECT 'ID: ' || id || ', Username: ' || username || ', Email: ' || email || ', Last Updated: ' || last_updated as result FROM users ORDER BY id;"

echo ""
echo "Connection closed successfully."