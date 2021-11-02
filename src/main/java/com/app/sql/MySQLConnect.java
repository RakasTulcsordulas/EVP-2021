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
     * Executes an INSERT INTO command into 'employee' table on a given connection. Passwords are hashed on server side, salt is a randomized 10 character string.
     * @param username varchar(30)
     * @param password varchar(224)
     * @param emailAddress varchar(50)
     */
    public void signUpNewEmployee (String username, String password, String emailAddress) {

        String query = "INSERT INTO employee (id, username, password, salt) VALUES (NULL, ?, ?, ?)";

        //Setting stored salted password
        byte[] array = new byte[10]; // salt length is bounded by 10
        new Random().nextBytes(array);
        String salt = new String(array, Charset.forName("UTF-8"));

        password = password + salt;

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, salt);

            int result = preparedStatement.executeUpdate();
            System.out.println(result);
            if (result > 0) {
                System.out.println("New employee is inserted into the database!");
            } else {
                System.out.println("Error! Incorrect parameters!");
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    public void deleteEmployee (String username) {
        String query = "DELETE FROM employee WHERE username = ?";

        System.out.println("Deleting employee...");
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
     * @param username user's name associated with the password
     * @param password password to be checked
     * @return true if given username and password matches in database
     */
    public boolean checkPassword(String username, String password) {
        String query = "SElECT employee.username FROM employee WHERE PASSWORD(CONCAT(?, employee.salt)) = employee.password AND employee.username = ?";

        System.out.println("Checking for username and password...");

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Success!");
                return true;
            }
        } catch (SQLException e) {e.printStackTrace();}
        System.out.println("Authentication failed!");
        return false;
    }

    /**
     * Executes a SELECT query on a given username and checks if it exists in the database.
     * @param username - the username to be checked
     * @return - true if at least one instance was found
     */
    public boolean checkUsernameExists(String username) {
        String query = "SELECT username FROM employee WHERE username = ?";

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

    //********************
    //** MOVIE HANDLING **
    //********************

    /**
     * Inserts a new movie into the database with the given parameters on an established database connection.
     * @param title varchar(128) NOTNULL
     * @param director varchar(256)
     * @param cast varchar(1024)
     * @param description text
     * @param duration_min int(11)
     */
    public void insertNewMovie(String title, String director, String cast, String description, String duration_min) {
        String query = "INSERT INTO movie (id, title, director, length, type, aspect, language, directors, category) VALUES (NULL, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1,title);
            preparedStatement.setString(2,director);
            preparedStatement.setString(3,cast);
            preparedStatement.setString(4,description);
            preparedStatement.setString(5,duration_min);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("New movie " + title + " is inserted into the database!");
            } else {
                System.out.println("Error! Incorrect parameters!");
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    /**
     * Deletes a movie with the given title and release_date combination
     * @param title title of the movie VARCHAR(128)
     */
    public void deleteMovie(String title) {
        String query = "DELETE FROM movie WHERE title = ?";

        System.out.println("Deleting movie...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setString(1, title);
            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Deletion successful!");
            } else {
                System.out.println("Movie was not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an SQL query on the 'movie' table with the given title.
     * @param title Title to be searched in the database, case-sensitive.
     * @return an array of object starting from 1, containing the records of the movie the order as stored in the database.
     */
    public Object[] getMovie(String title) {
        Object[] resultSetObject = null;
        String query = "SELECT * FROM movie WHERE title = ?";

        System.out.println("Searching for movie...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                resultSetObject = new Object[rsmd.getColumnCount()];

                for (int i = 1; i < rsmd.getColumnCount(); ++i) {
                    resultSetObject[i] = resultSet.getObject(i);
                }
            } else {return null;}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSetObject;
    }

}