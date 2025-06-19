#!/bin/bash

# PostgreSQL connection parameters
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=testdb
export PGUSER=testuser
export PGPASSWORD=testpass

echo "Disabling PostgreSQL logging..."

# Disable logging
psql -c "ALTER SYSTEM SET log_statement = 'none';"
psql -c "ALTER SYSTEM SET log_min_duration_statement = -1;"

# Reload configuration
psql -c "SELECT pg_reload_conf();"

echo "Logging disabled."