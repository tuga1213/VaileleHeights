/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Admin;

public interface AdminDAO {
    Admin getAdminByPassword(String password);
    void addAdmin(Admin admin);
    boolean adminExists(String email);
}
