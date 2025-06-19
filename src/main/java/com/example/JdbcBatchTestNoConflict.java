package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcBatchTestNoConflict {
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

            String upsertSQL = "INSERT INTO users (id, username, email) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET " +
                "username = EXCLUDED.username, email = EXCLUDED.email, last_updated = CURRENT_TIMESTAMP";

            PreparedStatement preparedStatement = connection.prepareStatement(upsertSQL);

            String[][] testData = {
                {"1", "john_doe", "john@example.com"},
                {"2", "jane_smith", "jane@example.com"},
                {"3", "mr smith 1", "mr.smith.1@example.com"},
                {"4", "mr smith 2", "mr.smith.2@example.com"},
                {"5", "mr smith 3", "mr.smith.3@example.com"},
                {"6", "mr smith 4", "mr.smith.4@example.com"},
                {"7", "mr smith 5", "mr.smith.5@example.com"},
                {"8", "mr smith 6", "mr.smith.6@example.com"},
                {"9", "mr smith 7", "mr.smith.7@example.com"},
                {"10", "mr smith 8", "mr.smith.8@example.com"},
            };

            System.out.println("Adding statements to batch...");
            for (String[] userData : testData) {
                preparedStatement.setInt(1, Integer.parseInt(userData[0]));
                preparedStatement.setString(2, userData[1]);
                preparedStatement.setString(3, userData[2]);
                preparedStatement.addBatch();
                System.out.println("Added to batch: ID " + userData[0] + ", " + userData[1] + ", " + userData[2]);
            }

            System.out.println("\nExecuting batch...");
            preparedStatement.executeBatch();
            preparedStatement.clearParameters();
            System.out.println("Batch execution completed!");

            ResultSet queryResult = statement.executeQuery("SELECT * FROM users ORDER BY id");
            System.out.println("\nCurrent users in database:");
            while (queryResult.next()) {
                System.out.println("ID: " + queryResult.getInt("id") + 
                                 ", Username: " + queryResult.getString("username") + 
                                 ", Email: " + queryResult.getString("email") + 
                                 ", Last Updated: " + queryResult.getTimestamp("last_updated"));
            }

            queryResult.close();
            preparedStatement.close();
            statement.close();
            connection.close();
            System.out.println("\nConnection closed successfully.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}