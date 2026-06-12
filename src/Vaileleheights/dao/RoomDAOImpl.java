/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.DoubleRoom;
import Vaileleheights.model.Room;
import Vaileleheights.model.SingleRoom;
import Vaileleheights.model.Suite;
import Vaileleheights.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private Connection conn;

    public RoomDAOImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setBoolean(4, room.isBooked());
            ps.executeUpdate();
            System.out.println("Room added to database.");
        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }

    @Override
    public boolean removeRoom(int roomNumber) {
        String sql = "DELETE FROM rooms WHERE roomNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error removing room: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int roomNumber = rs.getInt("roomNumber");
                String roomType = rs.getString("roomType");
                double price = rs.getDouble("pricePerNight");
                boolean isBooked = rs.getBoolean("isBooked");

                Room room;
                switch (roomType.toLowerCase()) {
                    case "single": room = new SingleRoom(roomNumber, price); break;
                    case "double": room = new DoubleRoom(roomNumber, price); break;
                    case "suite":  room = new Suite(roomNumber, price); break;
                    default:       room = new SingleRoom(roomNumber, price); break;
                }
                room.setBooked(isBooked);
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.out.println("Error getting rooms: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * FROM rooms WHERE roomNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String roomType = rs.getString("roomType");
                double price = rs.getDouble("pricePerNight");
                boolean isBooked = rs.getBoolean("isBooked");

                Room room;
                switch (roomType.toLowerCase()) {
                    case "single": room = new SingleRoom(roomNumber, price); break;
                    case "double": room = new DoubleRoom(roomNumber, price); break;
                    case "suite":  room = new Suite(roomNumber, price); break;
                    default:       room = new SingleRoom(roomNumber, price); break;
                }
                room.setBooked(isBooked);
                return room;
            }
        } catch (SQLException e) {
            System.out.println("Error getting room: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateRoomStatus(int roomNumber, boolean isBooked) {
        String sql = "UPDATE rooms SET isBooked = ? WHERE roomNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isBooked);
            ps.setInt(2, roomNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating room status: " + e.getMessage());
        }
    }
}
