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
    public void establishConnection() throws SQLException{

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

        } catch (SQLException e) {throw new SQLException(e);}
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
     */
    public void signUpNewEmployee (String username, String password) {

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
    public void insertNewMovie(String title, String director, String cast, String description, String rating, String duration_min) {
        String query = "INSERT INTO movie (id, title, director, cast, description, rating, duration_min) VALUES (NULL, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1,title);
            preparedStatement.setString(2,director);
            preparedStatement.setString(3,cast);
            preparedStatement.setString(4,description);
            preparedStatement.setString(5, rating);
            preparedStatement.setString(6,duration_min);

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
     * @param id Id of the movie. Can be null
     * @param title Title to be searched in the database, case-sensitive. Can be null
     * @param duration_min duration of the movie in minutes int(11). Can be null
     * @return an array of objects starting from 1, containing the records of the movie in the order as stored in the database.
     */
    public Object[] getMovie(Object id, Object title, Object duration_min) {
        Object[] resultSetObject = null;
        String query = "SELECT * FROM movie WHERE " +
                "(? IS NULL OR ? = id)" +
                "AND " +
                "(? IS NULL OR ? = title)" +
                "AND " +
                "(? IS NULL OR ? = duration_min)";

        System.out.println("Searching for movie...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, id);
            preparedStatement.setObject(3, title);
            preparedStatement.setObject(4, title);
            preparedStatement.setObject(5, duration_min);
            preparedStatement.setObject(6, duration_min);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                resultSetObject = new Object[rsmd.getColumnCount()];

                for (int i = 1; i < rsmd.getColumnCount(); ++i) {
                    resultSetObject[i] = resultSet.getObject(i);
                }
            } else {
                System.out.println("No movie is found.");
                return null;}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSetObject;
    }

    //*************************
    //** AUDITORIUM HANDLING **
    //*************************

    /**
     * Executes and INSERT INTO update on the auditorium table with the given parameters.
     * @param name name of the auditorium varchar(32)
     * @param seats_no total number of seats in the auditorium int(11)
     * @return Inserted auditorium id as an int
     */
    public int insertAuditorium (String name, int seats_no) {
        String query = "INSERT INTO auditorium (id, name, seats_no) VALUES (NULL, ?, ?)";

        System.out.println("Inserting new auditorium...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, seats_no);

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                System.out.println("New auditorium " + name + " is inserted into the database.");
                return resultSet.getInt(1);
            } else {
                System.out.println("Could not insert " + name + "into the database.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Executes an SQL query on the 'movie' table with the given title.
     * @param id Id of the auditorium. Can be null
     * @return an array of objects starting from 1, containing the records of the auditoriums in the order as stored in the database.
     */
    public Object[][] getAuditorium(Object id) {
        Object[][] resultSetObject = null;
        String query = "SELECT * FROM auditorium WHERE " +
                "(? IS NULL OR ? = id)";

        System.out.println("Searching for auditorium...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY))
        {
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.last();
            resultSetObject = new Object[resultSet.getRow()+1][];
            resultSet.beforeFirst();

            int j = 1;
            while (resultSet.next()) {

                ResultSetMetaData rsmd = resultSet.getMetaData();
                resultSetObject[j] = new Object[rsmd.getColumnCount()+1];
                for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                    resultSetObject[j][i] = resultSet.getObject(i);
                }

                j++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSetObject;
    }

    /**
     * Executes a DELETE SQL query on the 'Auditoirum' table with the given id.
     * @param id Id of the auditorium. Cannot be null
     */
    public void deleteAuditorium(int id) {
        String query = "DELETE FROM auditorium WHERE id = ?";

        System.out.println("Deleting auditorium...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Deletion successful!");
            } else {
                System.out.println("Auditorium was not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes an INSERT INTO update on the seat table with the given parameters.
     * @param row Row of the seat int(11).
     * @param number Number of the seat int(11).
     * @param auditorium_id Id of the auditorium containing the seat FK of auditorium int(11).
     */
    public void insertSeat (int row, int number, int auditorium_id) {
        String query = "INSERT INTO seat (id, row, number, auditorium_id) VALUES (NULL, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, row);
            preparedStatement.setInt(2, number);
            preparedStatement.setInt(3, auditorium_id);

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("New seat inserted");
            } else {
                System.out.println("Error! Insertion failed!");
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    /**
     * Executes a SELECT query on the screening table with any combination of the given parameters.
     * @param id Seat id int(11). Can be null.
     * @param row Row number int(11). Can be null.
     * @param number Seat's number in the row (i.e. column number) int(11). Can be null.
     * @param auditorium_id Auditorium's id FK_int(11). Can be null.
     * @return An Object[] starting from 1 containing the columns of all seats with at least matching the given attributes or null if no matching seat was found.
     */
    public Object[][] getSeat (Object id, Object row, Object number, Object auditorium_id) {
        Object[][] resultSetObject = null;
        String query = "SELECT * FROM seat WHERE " +
                "(? IS NULL OR ? = id) " +
                "AND " +
                "(? IS NULL OR ? = row) " +
                "AND " +
                "(? IS NULL OR ? = number) " +
                "AND " +
                "(? IS NULL OR ? = auditorium_id)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, id);
            preparedStatement.setObject(3, row);
            preparedStatement.setObject(4, row);
            preparedStatement.setObject(5, number);
            preparedStatement.setObject(6, number);
            preparedStatement.setObject(7, auditorium_id);
            preparedStatement.setObject(8, auditorium_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.last();
            resultSetObject = new Object[resultSet.getRow()+1][];
            resultSet.beforeFirst();

            int j = 1;
            while (resultSet.next()) {

                ResultSetMetaData rsmd = resultSet.getMetaData();
                resultSetObject[j] = new Object[rsmd.getColumnCount()+1];
                for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                    resultSetObject[j][i] = resultSet.getObject(i);
                }

                j++;
            }
        } catch (SQLException e) {e.printStackTrace();}
        return resultSetObject;
    }


    /**
     * Executes a DELETE SQL query on the 'Seat' table with the given auditorium id.
     * @param auditoriumId Id of the auditorium. Cannot be null
     */
    public void deleteSeats(int auditoriumId) {
        String query = "DELETE FROM seat WHERE auditorium_id = ?";

        System.out.println("Deleting seats...");
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setInt(1, auditoriumId);
            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                System.out.println("Deletion successful!");
            } else {
                System.out.println("Seats was not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //************************
    //** SCREENING HANDLING **
    //************************

    /**
     * Executes an INSERT INTO update on the screening table with the given parameters.
     * @param movie_id Id of the inserted movie FK of movie int(11).
     * @param auditorium_id Id of the auditorium having the screening FK of auditorium int(11).
     * @param screening_start Timestamp of the screening as [YYYY-MM-DD hh:mm:ss]
     */
    public void insertScreening (int movie_id, int auditorium_id, Timestamp screening_start) {
        String query = "INSERT INTO screening (id, movie_id, auditorium_id, screening_start) VALUES (NULL, ?, ?, ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setInt(1, movie_id);
            preparedStatement.setInt(2, auditorium_id);
            preparedStatement.setTimestamp(3, screening_start);

            if (preparedStatement.executeUpdate() > 0) {
                System.out.println("New screening inserted.");
            } else {
                System.out.println("Error! Insertion failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a SELECT query on the screening table with any combination of the given parameters.
     * @param id Id of the screening AUTO INCREMENT int(11).
     * @param movie_id Id of the inserted movie FK of movie int(11).
     * @param auditorium_id Id of the auditorium having the screening FK of auditorium int(11).
     * @param screening_start Timestamp of the screening as [YYYY-MM-DD hh:mm:ss]. Can be null
     * @return An Object[] starting from 1 containing in sequence every data of all rows where at least one given parameter is met.
     */
    public Object[] getScreening (Object id, Object movie_id, Object auditorium_id, Timestamp screening_start) {
        Object[] resultSetObject = null;
        String query = "SELECT * FROM screening WHERE " +
                "(? IS NULL OR ? = id) " +
                "AND " +
                "(? IS NULL OR ? = movie_id) " +
                "AND " +
                "(? IS NULL OR ? = auditorium_id) " +
                "AND " +
                "(? IS NULL OR ? = screening_start)";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, id);
            preparedStatement.setObject(3, movie_id);
            preparedStatement.setObject(4, movie_id);
            preparedStatement.setObject(5, auditorium_id);
            preparedStatement.setObject(6, auditorium_id);
            preparedStatement.setTimestamp(7, screening_start);
            preparedStatement.setTimestamp(8, screening_start);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                resultSetObject = new Object[rsmd.getColumnCount()];

                for (int i = 1; i < rsmd.getColumnCount(); ++i) {
                    resultSetObject[i] = resultSet.getObject(i);
                }
            } else {return null;}
        } catch (SQLException e) {e.printStackTrace();}
        return resultSetObject;
    }

    //**************************
    //** RESERVATION HANDLING **
    //**************************


}