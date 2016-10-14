/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
class MessageManager extends Thread {
    private DataInputStream dis;
    private DataOutputStream dos;
    ServerSocket serverSock ;
    Socket clientSock;
    
    int myID;
    int toID;
    MessageManager(Socket cliSock,int clientID, int toID) {
        clientSock = cliSock;
        serverSock = LiveStreamServer.msgServerSock;
        this.myID = clientID;
        this.toID = toID ;
        System.out.println("Managing from " + myID + " to " + this.toID);
        new AudioManager(clientID,toID).start();
    }
    
    @Override
    public void run () {
        dis = LiveStreamServer.clientInfo[myID].getDataInputStream();
        while (true) {
            try {String msg = dis.readUTF();
                dos=LiveStreamServer.clientInfo[toID].getDataOutputStream();
                if(msg.equals("logout"))
                    break;
                dos.writeUTF(msg);
            } catch (IOException ex) {
                System.out.println("Chud gaya main !!\n");
                Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
