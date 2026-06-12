/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Admin;
import Vaileleheights.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImpl implements AdminDAO {

    private Connection conn;

    public AdminDAOImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Admin getAdminByPassword(String password) {
        String sql = "SELECT * FROM admins WHERE password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding admin: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addAdmin(Admin admin) {
        String sql = "INSERT INTO admins (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getAdminCode());
            ps.executeUpdate();
            System.out.println("Admin added to database.");
        } catch (SQLException e) {
            System.out.println("Error adding admin: " + e.getMessage());
        }
    }

    @Override
    public boolean adminExists(String email) {
        String sql = "SELECT COUNT(*) FROM admins WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking admin: " + e.getMessage());
        }
        return false;
    }
}
