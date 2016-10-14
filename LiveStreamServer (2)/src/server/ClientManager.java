/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
public class ClientManager extends Thread {
    int clientID ;
    ServerSocket sock;
    Socket cliSock;
    private DataInputStream dis;
    private  DataOutputStream dos;
    public ClientManager() {
        
        sock = LiveStreamServer.msgServerSock;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                cliSock = sock.accept();
                dis = new DataInputStream(cliSock.getInputStream());
                dos = new DataOutputStream(cliSock.getOutputStream());
                SocketAddress remoteSocketAddress = cliSock.getRemoteSocketAddress();
                System.out.println(remoteSocketAddress);
                
                int fromID = dis.readInt();
                int toID = dis.readInt();
                clientID=fromID;
                LiveStreamServer.clientInfo[clientID].setStreams(dis,dos,cliSock);
                
                MessageManager m12 = new MessageManager(cliSock,clientID,toID); // MANAGING MESSAGES FROM 1 to 2
                System.out.println("Calling Constructors");
                // MANAGING MESSAGES FROM 2 to 1
                m12.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
