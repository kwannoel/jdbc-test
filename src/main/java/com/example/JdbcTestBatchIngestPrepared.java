package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcTestBatchIngestPrepared {
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
                "INSERT INTO users (id, username, email) VALUES (?, ?, ?);" +
                "INSERT INTO users (id, username, email) VALUES (?, ?, ?);";
            
            PreparedStatement preparedStatement = connection.prepareStatement(upsertSQL);

            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "john_doe");
            preparedStatement.setString(3, "john@example.com");

            preparedStatement.setInt(4, 2);
            preparedStatement.setString(5, "jane_smith");
            preparedStatement.setString(6, "jane@example.com");

            preparedStatement.execute();

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