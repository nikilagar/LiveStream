package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vikrant
 */
public class Client {
    
    public static final String SERVERIP = "172.31.78.96";
    
    public static void main (String[] args) {
        LoginPanel reg = new LoginPanel();
        reg.setTitle("LiveStream Login");
        reg.setVisible(true);
        reg.setLocationRelativeTo(null);
    } 
}