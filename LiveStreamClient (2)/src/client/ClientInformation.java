/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
public class ClientInformation implements Serializable {
    private final int clientID ;
    private final String userName ;
    private final String name ;
    private final String gender ;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    
    // Getters
    
    ClientInformation(int id, String uname, String nam, String gen, Socket sock) {
        clientID = id;
        userName = uname;
        name = nam ;
        gender = gen;
        try {
            ois = new ObjectInputStream(sock.getInputStream());
            oos = new ObjectOutputStream(sock.getOutputStream());
            dis = new DataInputStream(sock.getInputStream());
            dos = new DataOutputStream(sock.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ClientInformation(int id, String uname, String nam, String gen) {
        clientID = id;
        userName = uname;
        name = nam;
        gender = gen;
    }
    
    public int getID () {
        return clientID;
    }
    
    public String getUserName() {
        return userName ;
    }
    
    public String getName() {
        return name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public ObjectInputStream getObjectInputStream() {
        return ois; 
    }
    
    public ObjectOutputStream getObjectOutputStream() {
        return oos ;
    }
    
    public DataInputStream getDataInputStream() {
        return dis ;
    }
    
    public DataOutputStream getDataOutputStream() {
        return dos ;
    }
}
