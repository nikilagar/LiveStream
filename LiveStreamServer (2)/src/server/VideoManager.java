/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import share.VideoImageObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
class VideoManager extends Thread {
    private DataInputStream dis;
    private DataOutputStream dos;
    ServerSocket serverSock ;
    Socket clientSock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    int myID;
    int toID;
    VideoManager(Socket cliSock,int clientID, int toID) {
        serverSock = LiveStreamServer.videoServerSock;
        clientSock = cliSock;
        this.myID = clientID;
        this.toID = toID;
        System.out.println("Video from " + clientID + " to " + toID);
    }
    
    @Override
    public void run () {
//        ois = LiveStreamServer.clientInfo[myID].getVideoInput();
//        oos = LiveStreamServer.clientInfo[toID].getVideoOutput();
        while (true) {
            ois = LiveStreamServer.clientInfo[myID].getVideoInput();
            oos = LiveStreamServer.clientInfo[toID].getVideoOutput();
            try { 
                VideoImageObject lol = (VideoImageObject)ois.readObject();
                if (lol.img == null) {
                    break;
                }
                if (oos != null) {
                    oos.writeObject(lol);
                    oos.reset();
                    oos.flush();
                }
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(VideoManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
