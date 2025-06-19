#!/bin/bash

# PostgreSQL connection parameters
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=testdb
export PGUSER=testuser
export PGPASSWORD=testpass

echo "Enabling PostgreSQL INSERT logging..."

# Enable logging for all statements
psql -c "ALTER SYSTEM SET log_statement = 'all';"
psql -c "ALTER SYSTEM SET log_min_duration_statement = 0;"
psql -c "ALTER SYSTEM SET log_line_prefix = '%t [%p]: [%l-1] user=%u,db=%d,app=%a,client=%h ';"

# Reload configuration
psql -c "SELECT pg_reload_conf();"

echo "Logging enabled. To view logs, run:"
echo "docker logs jdbc-test-postgres -f"
echo ""
echo "To disable logging later, run ./disable_logging.sh"