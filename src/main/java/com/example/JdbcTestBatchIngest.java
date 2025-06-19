package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcTestBatchIngest {
    private static final String URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to PostgreSQL database!");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT version()");

            if (resultSet.next()) {
                System.out.println("PostgreSQL version: " + resultSet.getString(1));
            }
            resultSet.close();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            statement.execute(createTableSQL);
            System.out.println("Table 'users' created successfully!");

            String upsertSQL =
                "INSERT INTO users (id, username, email) VALUES (1, 'john_doe', 'john@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (2, 'jane_smith', 'jane@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (3, 'mr smith 1', 'mr.smith.1@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (4, 'mr smith 2', 'mr.smith.2@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (5, 'mr smith 3', 'mr.smith.3@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (6, 'mr smith 4', 'mr.smith.4@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (7, 'mr smith 5', 'mr.smith.5@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (8, 'mr smith 6', 'mr.smith.6@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (9, 'mr smith 7', 'mr.smith.7@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (10, 'mr smith 8', 'mr.smith.8@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (11, 'mr smith 9', 'mr.smith.9@example.com');" +
                "INSERT INTO users (id, username, email) VALUES (12, 'mr smith 10', 'mr.smith.10@example.com');";

            statement.executeBatch(upsertSQL);

            ResultSet queryResult = statement.executeQuery("SELECT * FROM users ORDER BY id");
            System.out.println("\nCurrent users in database:");
            while (queryResult.next()) {
                System.out.println("ID: " + queryResult.getInt("id") + 
                                 ", Username: " + queryResult.getString("username") + 
                                 ", Email: " + queryResult.getString("email") + 
                                 ", Last Updated: " + queryResult.getTimestamp("last_updated"));
            }

            queryResult.close();
            statement.close();
            connection.close();
            System.out.println("\nConnection closed successfully.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}