/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;

/**
 *
 * @author vikrant
 */
public class AudioCallActivity extends javax.swing.JFrame implements Runnable {
    final int tenKB = 10240;
    final String SERVER_IP = Client.SERVERIP;
    TargetDataLine line;
    SourceDataLine sourceLine ;
    DatagramSocket sendSocket = null;
    DatagramSocket recSocket = null;
    int from = 0;
    int to = 0;
    
    Queue < byte[] > queue; 
    
    /**
     * Creates new form AudioCallActivity
     */
    AudioCallActivity(int fromID, int toID) {
        queue = new LinkedList();
        try {
            initComponents();
            from = fromID;
            to = toID;
            sendSocket = new DatagramSocket(4000 + fromID);
            recSocket = new DatagramSocket(fromID + 20000);
        } catch (SocketException ex) {
            Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,channels, signed, bigEndian);
        return format;
    }
    
    

    @Override
    public void run() {
        new AudioSender().start();
        new AudioReceiver().start();
    }
    
    private class AudioSender extends Thread {
        @Override
        public void run () {
            try {
                AudioFormat format = getAudioFormat();
                Info info = new Info (TargetDataLine.class,format);

                line = (TargetDataLine)AudioSystem.getLine(info);
                byte [] buffer = new byte [tenKB];

                line.open(format);
                line.start();

                while (true) {
                    line.read(buffer, 0, tenKB);
                    DatagramPacket pack = new DatagramPacket(buffer, 0 , tenKB, 
                            InetAddress.getByName(SERVER_IP),to+10000);
                    //System.out.println("SENDING TO " + InetAddress.getByName(SERVER_IP) + ":" + (to+10000));
                    sendSocket.send(pack);
                    buffer = new byte [tenKB];
                }
            } catch (LineUnavailableException ex) {
                Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private class AudioReceiver extends Thread {
        @Override
        public void run () {
            try {
                AudioFormat format = getAudioFormat();
                Info info = new Info (SourceDataLine.class,format);
                sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open(format);
                sourceLine.start();
                
                new PlayAudio().start();
                
                byte [] buffer = new byte[tenKB];
                
                while (true) {
                    DatagramPacket pack = new DatagramPacket(buffer,tenKB);
                    recSocket.receive(pack);
                    System.out.println("Receiving from " + pack.getAddress() + ":" + pack.getPort());
                    queue.add(pack.getData());
                }
            } catch (LineUnavailableException ex) {
                Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    private class PlayAudio extends Thread {
        @Override
        public void run () {
            while(true){
                if(!queue.isEmpty()){
                    sourceLine.write(queue.peek(),0,tenKB);
                    queue.remove();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AudioCallActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        endCallBut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Call in Progress..");

        endCallBut.setText("End Call");
        endCallBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endCallButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(endCallBut)
                .addContainerGap(112, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(endCallBut)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void endCallButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endCallButActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endCallButActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton endCallBut;
    // End of variables declaration//GEN-END:variables
 
}
