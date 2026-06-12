/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:derby:" + System.getProperty("user.dir") + "/VaileleHeightsDB;create=true";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    private DatabaseConnection() {
        try {
            Class.forName("org.apache.derby.iapi.jdbc.AutoloadedDriver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connected successfully.");
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() {
        createRoomsTable();
        createGuestsTable();
        createBookingsTable();
    }

    private void createRoomsTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE rooms (" +
                "roomNumber INT PRIMARY KEY," +
                "roomType VARCHAR(20)," +
                "pricePerNight DOUBLE," +
                "isBooked BOOLEAN" +
                ")"
            );
            System.out.println("Rooms table created.");
            insertDefaultRooms();
        } catch (SQLException e) {
            System.out.println("Rooms table already exists.");
        }
    }

    private void insertDefaultRooms() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("INSERT INTO rooms VALUES (101, 'Single', 100.0, false)");
            stmt.executeUpdate("INSERT INTO rooms VALUES (102, 'Double', 150.0, false)");
            stmt.executeUpdate("INSERT INTO rooms VALUES (103, 'Suite', 250.0, false)");
            System.out.println("Default rooms inserted.");
        } catch (SQLException e) {
            System.out.println("Error inserting default rooms: " + e.getMessage());
        }
    }

    private void createGuestsTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE guests (" +
                "guestId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "name VARCHAR(100)," +
                "email VARCHAR(100)," +
                "phone VARCHAR(20)" +
                ")"
            );
            System.out.println("Guests table created.");
        } catch (SQLException e) {
            System.out.println("Guests table already exists.");
        }
    }

    private void createBookingsTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE bookings (" +
                "bookingId VARCHAR(10) PRIMARY KEY," +
                "guestName VARCHAR(100)," +
                "guestEmail VARCHAR(100)," +
                "guestPhone VARCHAR(20)," +
                "roomNumber INT," +
                "roomType VARCHAR(20)," +
                "pricePerNight DOUBLE," +
                "startDate VARCHAR(20)," +
                "endDate VARCHAR(20)" +
                ")"
            );
            System.out.println("Bookings table created.");
        } catch (SQLException e) {
            System.out.println("Bookings table already exists.");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
