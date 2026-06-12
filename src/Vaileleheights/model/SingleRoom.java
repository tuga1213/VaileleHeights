/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vaileleheights.model;

/**
 *
 * @author 
 * Falatugatuga Kerslake
 * 22181971
 */
public class SingleRoom extends Room {
    public SingleRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, "Single", pricePerNight);
    }

    @Override
    public String getRoomInfo() {
        return "Single Room | Room " + getRoomNumber()
             + " | $" + getPrice() + "/night | Max 1 guest";
    }
}

