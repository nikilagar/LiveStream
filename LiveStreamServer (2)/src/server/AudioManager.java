/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.IOException;
import static server.LiveStreamServer.udpSockets;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vikrant
 */
class AudioManager extends Thread {
    DatagramSocket sock = null ;
    final int tenKB = 10240;
    final int FROM_PORT ;
    final int TO_PORT ;
    int from = 0;
    int to = 0;

    public AudioManager(int fromID, int toID) {
        from = fromID;
        to = toID;
        FROM_PORT = 10000 + from;
        TO_PORT = 20000 + to;
        try {
            if (udpSockets[from] == null) {
                udpSockets[from] = new DatagramSocket(FROM_PORT);
                System.out.println("CREATED " + udpSockets[from]);
            }
        } catch (SocketException ex) {
            Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        while (true) {
            
            try {
                byte [] buffer = new byte [tenKB];
                DatagramPacket pack = new DatagramPacket(buffer,tenKB);
                System.out.println("WAITING " + udpSockets[from]);
                udpSockets[from].receive(pack);
                System.out.println("RECEIVED FROM " + pack.getAddress() + ":" + pack.getPort());
                pack = new DatagramPacket(buffer, tenKB, LiveStreamServer.clientInfo[from].getIPAddress(),20000+from);
                System.out.println(from + " TO " + to + " SENDING tO " + LiveStreamServer.clientInfo[from].getIPAddress() + ":" + 20000+from);
                udpSockets[from].send(pack);
            } catch (IOException ex) {
                Logger.getLogger(AudioManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
