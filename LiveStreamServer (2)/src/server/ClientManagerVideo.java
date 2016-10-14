/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
class ClientManagerVideo extends Thread {
    int clientID ;
    ServerSocket sock;
    Socket cliSock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public ClientManagerVideo() {
        this.cliSock = null;
        sock = LiveStreamServer.videoServerSock;
    }

    @Override
    public void run() {
        try {
            while (true) {
                cliSock = sock.accept();
                DataInputStream dis = new DataInputStream(cliSock.getInputStream());
                
                SocketAddress remoteSocketAddress = cliSock.getRemoteSocketAddress();
                System.out.println(remoteSocketAddress);
                int fromID = dis.readInt();
                int toID = dis.readInt();
                
                System.out.println("VIDEO FROM " + fromID + " TO " + toID);
                
                clientID=fromID;
                
                oos = new ObjectOutputStream(cliSock.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(cliSock.getInputStream());
             
                LiveStreamServer.clientInfo[clientID].setVideoStreams(ois,oos,cliSock);
                new VideoManager(cliSock,clientID,toID).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}