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

//TODO handling all exceptions
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

        byte[] array = new byte[10]; // salt length is bounded by 10
        new Random().nextBytes(array);
        String salt = new String(array, Charset.forName("UTF-8"));

        //Setting stored password
        password = password + salt;

        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("   INSERT INTO `users` (`id`, `username`, `password`, `salt`, `emailAddress`) " +
                                            "VALUES (NULL, " +  //id auto increments
                                            "'" + username +"', " +
                                            "'" + password +"', " +
                                            "'" + salt +"', " +
                                            "'" + emailAddress +"')");

        } catch (SQLException e) {e.printStackTrace();}
    }

    //TODO it would probably be better to fold these into one query for faster operation
    /**
     * Executes a SELECT query on a given username and checks if it exists in the database.
     * @param username - the username to be checked
     * @return - true if at least one instance was found
     */
    public boolean checkUsernameExists(String username) {
        String query = "SELECT username FROM `users` WHERE username = '"+ username +"'";

        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

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
        String query = "SELECT emailAddress FROM `users` WHERE emailAddress = '"+ emailAddress +"'";

        try (Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

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
     *
     * @param title varchar(128) NOTNULL
     * @param release_date int(11) NOTNULL
     * @param length int(11)
     * @param type varchar(16)
     * @param aspect varchar(16)
     * @param language varchar(128)
     */
    public void insertNewMovie(String title, int release_date, int length, String type, String aspect, String language) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("   INSERT INTO `movies` (`id`, `title`, `release_date`, `length`, `type`, `aspect`, `language`) " +
                    "VALUES (NULL, " +              //id auto increments
                    "'" + title +"', " +            //NOTNULL
                    "'" + release_date +"', " +
                    "'" + length +"', " +
                    "'" + type +"', " +
                    "'" + aspect +"', " +
                    "'" + language +"')");

        } catch (SQLException e) {e.printStackTrace();}
    }

}