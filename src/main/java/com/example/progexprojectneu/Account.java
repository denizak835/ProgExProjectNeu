package com.example.progexprojectneu;

import java.sql.*;

public class Account {
    private final String url = "jdbc:mysql://localhost:3306/db";
    private String username = "user";
    private String password = "password";
    private String EMail = "testmail@test.de";

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account() {
    }

    public int registration(String username, String password, String email) {
        try (Connection connection = DriverManager.getConnection(url, this.username, this.password);
             PreparedStatement accountStatement = connection.prepareStatement("INSERT INTO accounts (username, password, email) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement userStatement = connection.prepareStatement("CREATE USER ?@'localhost' IDENTIFIED BY ?")) {

            // Insert account
            accountStatement.setString(1, username);
            accountStatement.setString(2, password);
            accountStatement.setString(3, email);
            accountStatement.executeUpdate();

            ResultSet generatedKeys = accountStatement.getGeneratedKeys();
            int accountId = -1;
            if (generatedKeys.next()) {
                accountId = generatedKeys.getInt(1);
            }

            // Insert user
            userStatement.setString(1, username);
            userStatement.setString(2, password);
            userStatement.executeUpdate();


            grantPermissionsToAccount(username,"clothing");

            System.out.println("Account and user successfully created!");
            return accountId;
        } catch (SQLException e) {
            System.out.println("Error while creating account/user: " + e.getMessage());
        }

        return -1; // Error occurred, return -1
    }


    public void grantPermissionsToAccount(String accountName, String tableName) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String grantQuery = "GRANT UPDATE, SELECT ON " + tableName + " TO '" + accountName + "'@'localhost'";
            statement.executeUpdate(grantQuery);

            System.out.println("Permissions granted successfully!");
        } catch (SQLException e) {
            System.out.println("Error granting permissions: " + e.getMessage());
        }
    }



    public boolean login(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "denizdeniz");
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM accounts WHERE username = '" + username + "' AND password = '" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);

            boolean loggedIn = resultSet.next();

            resultSet.close();
            statement.close();
            connection.close();

            return loggedIn;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    }
