package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcTest {
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
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            statement.execute(createTableSQL);
            System.out.println("Table 'users' created successfully!");

            String upsertSQL = "INSERT INTO users (username, email) VALUES (?, ?) " +
                "ON CONFLICT (username) DO UPDATE SET " +
                "email = EXCLUDED.email, last_updated = CURRENT_TIMESTAMP";

            PreparedStatement preparedStatement = connection.prepareStatement(upsertSQL);

            String[][] testData = {
                {"john_doe", "john@example.com"},
                {"jane_smith", "jane@example.com"},
                {"john_doe", "john.doe@updated.com"}
            };

            for (String[] userData : testData) {
                preparedStatement.setString(1, userData[0]);
                preparedStatement.setString(2, userData[1]);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Upserted user: " + userData[0] + " (" + rowsAffected + " row affected)");
            }

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