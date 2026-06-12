/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.model;

import Vaileleheights.model.Room;

/**
 *
 * @author 
 * Falatugatuga Kerslake
 * 22181971
 */
public class DoubleRoom extends Room {
    public DoubleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, "Double", pricePerNight);
    }

    @Override
    public String getRoomInfo() {
        return "Double Room | Room " + getRoomNumber()
             + " | $" + getPrice() + "/night | Max 2 guests";
    }
}

