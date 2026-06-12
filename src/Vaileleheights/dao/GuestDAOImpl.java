/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Guest;
import Vaileleheights.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuestDAOImpl implements GuestDAO {

    private Connection conn;

    public GuestDAOImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getEmail());
            ps.setString(3, guest.getPhone());
            ps.executeUpdate();
            System.out.println("Guest saved to database.");
        } catch (SQLException e) {
            System.out.println("Error adding guest: " + e.getMessage());
        }
    }

    @Override
    public Guest getGuestByName(String name) {
        String sql = "SELECT * FROM guests WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Guest(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting guest: " + e.getMessage());
        }
        return null;
    }
}
