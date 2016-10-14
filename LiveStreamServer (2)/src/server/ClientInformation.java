/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
public class ClientInformation {

    private final int clientID;
    private final String userName;
    private final String name;
    private final String gender;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private ObjectInputStream videoInput = null;
    private ObjectOutputStream videoOutput = null;
    private Socket sock = null;
    private Socket videoSock = null;
    private DatagramSocket audioSock = null;
    private InetAddress clientIP ;
    // Getters

    ClientInformation(int id, String uname, String nam, String gen, Socket sock) {
        clientID = id;
        userName = uname;
        name = nam;
        gender = gen;
        try {
            dis = new DataInputStream(sock.getInputStream());
            dos = new DataOutputStream(sock.getOutputStream());
            this.sock = sock;
            clientIP = sock.getInetAddress();
        } catch (IOException ex) {
            Logger.getLogger(ClientInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getID() {
        return clientID;
    }

    public Socket getSocket() {
        return sock;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }

    public ObjectInputStream getVideoInput() {
        return videoInput;
    }
    
    public ObjectOutputStream getVideoOutput() {
        return videoOutput;
    }
    
    public void setStreams(DataInputStream i, DataOutputStream o, Socket s) {
        dis = i;
        dos = o;
        sock = s;
    }

    public void setVideoStreams(ObjectInputStream i, ObjectOutputStream o, Socket s) {
        videoInput = i;
        videoOutput = o;
        videoSock = s;
    }

    public void setAudioStreams(DatagramSocket s) {
        audioSock = s;
    }
    
    public InetAddress getIPAddress() {
        return clientIP;
    }
}
