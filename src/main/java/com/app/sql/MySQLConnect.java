/**
 *  Methods for basic SQL operations with MySQL database
 *
 * @author Dávid Attila (t1kjui)
 * @since 2021.09.24
 */

package com.app.sql;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.Random;

//TODO handling all exceptions
public class MySQLConnect {

    private Connection con;

    /**
     * Establishes MySQL database connection, works as setter <br>
     * TODO read connection parameters from JSON/config for easier setup
     *
     * @throws ClassNotFoundException on driver failure
     * @throws SQLException on MySQL failure
     */
    public void establishConnection() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/evp-2021";
        String username = "root";
        String password = "";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {e.printStackTrace();}

        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("MySQL Connected");

        } catch (SQLException e) {e.printStackTrace();}
    }

    /**
     * Executes an INSERT INTO command into 'users' table on a given connection. Passwords are hashed on server side, salt is a randomized 10 character string.
     *
     * @param username varchar(30)
     * @param password varchar(224)
     * @param emailAddress varchar(50)
     * @throws SQLException on SQL command failure
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
     * @throws SQLException - on SQL query error
     */
    public boolean checkUsernameExists(String username) throws SQLException {
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
     * @throws SQLException - on SQL query error
     */
    public boolean checkEmailExists(String emailAddress) throws SQLException {
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

    /**
     * Closes database connection, quasi works as destructor but not really.
     *
     * @throws SQLException on SQL command failure
     */
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {e.printStackTrace();}
    }
}