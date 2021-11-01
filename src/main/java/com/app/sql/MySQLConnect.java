/**
 *  Methods for basic SQL operations with MySQL database
 *
 * @author Dávid Attila (t1kjui)
 * @since 2021.09.24
 */

package com.app.sql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class MySQLConnect {

    //*************************
    //** CONNECTION HANDLING **
    //*************************

    private Connection con;

    /**
     * Establishes MySQL database connection, works as a setter <br>
     * Database parameters are read from database.conf
     *
     */
    public void establishConnection() {

        Properties prop = new Properties();
        String configFile = "src/main/resources/database.conf";

        try (FileInputStream fis = new FileInputStream(configFile)) {
            prop.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(prop.getProperty("driver"));
        } catch (ClassNotFoundException e) {e.printStackTrace();}

        try {
            con = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
            System.out.println("MySQL Connected");

        } catch (SQLException e) {e.printStackTrace();}
    }

    /**
     * Closes database connection, quasi works as destructor but not really.
     *
     */
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {e.printStackTrace();}
    }


    //*******************
    //** USER HANDLING **
    //*******************

    /**
     * Executes an INSERT INTO command into 'users' table on a given connection. Passwords are hashed on server side, salt is a randomized 10 character string.
     *
     * @param username varchar(30)
     * @param password varchar(224)
     * @param emailAddress varchar(50)
     */
    public void signUpNewUser(String username, String password, String emailAddress) {

        String query = "INSERT INTO users (id, username, password, salt, emailAddress) VALUES (NULL, ?, ?, ?, ?)";

        //Setting stored salted password
        byte[] array = new byte[10]; // salt length is bounded by 10
        new Random().nextBytes(array);
        String salt = new String(array, Charset.forName("UTF-8"));

        password = password + salt;

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, salt);
            preparedStatement.setString(4, emailAddress);

            int result = preparedStatement.executeUpdate();
            System.out.println(result);
            if (result > 0) {
                System.out.println("New user is inserted into the database!");
            } else {
                System.out.println("Error! Incorrect parameters!");
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    public void deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";

        System.out.println("Deleting user...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setString(1, username);
            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Deletion successful!");
            } else {
                System.out.println("User was not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a username + password combination exists in the database.
     *
     * @param username user's name associated with the password
     * @param password password to be checked
     * @return true if given username and password matches in database
     */
    public boolean checkPassword(String username, String password) {
        String query = "SElECT users.username FROM users WHERE PASSWORD(CONCAT(?, users.salt)) = users.password AND users.username = ?";

        System.out.println("Checking for username and password...");

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Success!");
                return true;
            }
        } catch (SQLException e) {e.printStackTrace();};
        System.out.println("Authentication failed!");
        return false;
    }

    /**
     * Executes a SELECT query on a given username and checks if it exists in the database.
     * @param username - the username to be checked
     * @return - true if at least one instance was found
     */
    public boolean checkUsernameExists(String username) {
        String query = "SELECT username FROM users WHERE username = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Executes a SELECT query on a given email address and checks if it exists in the database.
     * @param emailAddress - the email address to be checked
     * @return - true if at least one instance was found
     */
    public boolean checkEmailExists(String emailAddress) {
        String query = "SELECT emailAddress FROM users WHERE emailAddress = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, emailAddress);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //********************
    //** MOVIE HANDLING **
    //********************

    //TODO test this
    /**
     * Inserts a new movie into the database with the given parameters on an established database connection.
     *
     * @param title varchar(128) NOTNULL
     * @param release_date int(11) NOTNULL
     * @param length int(11)
     * @param type varchar(16)
     * @param aspect varchar(16)
     * @param language varchar(128)
     */
    public void insertNewMovie(String title, int release_date, int length, String type, String aspect, String language) {
        String query = "INSERT INTO movies (id, title, release_date, length, type, aspect, language) VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query);) {

            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, release_date);
            preparedStatement.setInt(3, length);
            preparedStatement.setString(4,type);
            preparedStatement.setString(5, aspect);
            preparedStatement.setString(6, language);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("New movie " + title + " " + release_date + " is inserted into the database!");
            } else {
                System.out.println("Error! Incorrect parameters!");
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

}