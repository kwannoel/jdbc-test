package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
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
            statement.close();
            connection.close();
            System.out.println("Connection closed successfully.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}