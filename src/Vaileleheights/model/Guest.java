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
public class Guest extends User {
    private String phone;

    public Guest(String name, String email, String phone) {
        super(name, email);
        this.phone = phone;
    }

    @Override
    public String getRole() { return "Guest"; }

    public String getPhone() { return phone; }
}

