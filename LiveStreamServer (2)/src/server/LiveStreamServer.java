/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
public class LiveStreamServer {
    public static DatagramSocket[] udpSockets;
    public static ServerSocket servSock ;
    public static ClientInformation[] clientInfo; 
    public static boolean[] isConnected;
    public static ServerSocket msgServerSock;
    public static ServerSocket videoServerSock;
    public static ServerSocket audioServerSock;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        clientInfo = new ClientInformation[5001];
        isConnected = new boolean[5001];
        udpSockets = new DatagramSocket[5001];
        initializeUser();
    }    
    
    private static void initializeUser () {
        try {            
            servSock = new ServerSocket(1234);
            msgServerSock = new ServerSocket(4321);
            videoServerSock = new ServerSocket(3000);
            audioServerSock = new ServerSocket(2000);
        } catch (IOException ex) {
            Logger.getLogger(LiveStreamServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new ClientManager().start();
        new ClientManagerVideo().start();

        while (true) {
            try {
                Socket sock = servSock.accept();
                DataInputStream dis = new DataInputStream(sock.getInputStream());
                int id = dis.readInt();
                if (isConnected[id] == false) {
                    clientInfo[id] = DBConnector.getInfo(id,sock);
                    isConnected[id] = true;
                    System.out.println("Client " + sock.getInetAddress() + " connected !! :)");
                } else {
                    clientInfo[id].getSocket().close();
                    clientInfo[id].getVideoInput().close();
                    clientInfo[id].getVideoOutput().close();
                    clientInfo[id].getDataInputStream().close();
                    clientInfo[id].getDataOutputStream().close();
                    clientInfo[id] = null ;
                    isConnected[id] = false ;
                    System.out.println("Client " + id + " logged out !! :)");
                }
                dis.close();
            } catch (IOException ex) {
                Logger.getLogger(LiveStreamServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
