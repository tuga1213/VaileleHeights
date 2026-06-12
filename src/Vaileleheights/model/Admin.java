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
public class Admin extends User {
    private String adminCode;

    public Admin(String name, String email, String adminCode) {
        super(name, email);
        this.adminCode = adminCode;
    }

    @Override
    public String getRole() { return "Admin"; }

    public boolean authenticate(String code) {
        return this.adminCode.equals(code);
    }
}

