/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vaileleheights.dao;

import Vaileleheights.model.Guest;

public interface GuestDAO {
    void addGuest(Guest guest);
    Guest getGuestByName(String name);
}
