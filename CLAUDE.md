# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A simple JDBC testing project that connects to a PostgreSQL database running in Docker and executes a basic query.

## Common Commands

### Start PostgreSQL Database
```bash
make start
```

### Stop PostgreSQL Database
```bash
make stop
```

### Build Project
```bash
make compile
```

### Run JDBC Test
```bash
make test
```

## Architecture

- **JdbcTest.java**: Main class that connects to PostgreSQL and runs a simple `SELECT version()` query
- **docker-compose.yml**: PostgreSQL database configuration
- **pom.xml**: Maven configuration with PostgreSQL JDBC driver dependency

## Database Configuration

- Database: `testdb`
- User: `testuser`
- Password: `testpass`
- Port: `5432`