/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import share.VideoImageObject;
import com.github.sarxos.webcam.Webcam;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author vikrant
 */
public class VideoCallWindow extends javax.swing.JFrame implements Runnable {
    private boolean vidOrScreen ;
    Webcam webcam = null;
    Robot robot = null;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Socket videoSock ;
    
    int fromID,toID;
    public VideoCallWindow(int fromID, int toID, Socket sock) {
        initComponents();
        this.toID = toID;
        vidOrScreen = true ;
        this.fromID = fromID;
        try {
            videoSock = sock;
            DataOutputStream output = new DataOutputStream(videoSock.getOutputStream());
            output.writeInt(fromID);
            output.writeInt(toID);
            
            oos = new ObjectOutputStream(videoSock.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(videoSock.getInputStream());
            webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(640,480));
        }
        catch (IOException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("CONSTRUCTOR STARTED BRO... PARTY IS ON BITCH !!!");
        /* 
        *  Start receiving and sending video
        */
    }

    @Override
    public void run() {
        new UpdateMyVideo(this).start();
        new ReceiveVideo(this).start();
    }
    
    private class ReceiveVideo extends Thread {
        
        JFrame frame;
        ReceiveVideo(JFrame frame) {
            this.frame = frame;
        }
        
        @Override
        public void run() {
            System.out.println("Started receiving your video bitch");
            while(true){
                try {
                    final VideoImageObject receiveImg = (VideoImageObject) ois.readObject();
                    new Thread(new Runnable () {

                        @Override
                        public void run() {
                            Graphics graphics = friendVideo.getGraphics();
                            graphics.drawImage(receiveImg.img.getImage(), friendVideo.getWidth(), 0, -friendVideo.getWidth(),friendVideo.getHeight(),friendVideo);
                        }
                        
                    }).start();
                    
                } catch (ClassNotFoundException | IOException ex) {
                    Logger.getLogger(VideoCallWindow.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            
        }
    }
    
    private class UpdateMyVideo extends Thread {
        JFrame frame;
        UpdateMyVideo(JFrame frame) {
            this.frame = frame;
        }
        
        private BufferedImage getImage() {
            BufferedImage buff = null;
            if (vidOrScreen) {
                if (!webcam.isOpen()) {
                    webcam.open();
                }
                buff = webcam.getImage();
            } else {
                try {
                    robot = new Robot();
                    buff = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                } catch (AWTException ex) {
                    Logger.getLogger(VideoCallWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return buff;
        }
        
        @Override
        public void run() {
            Image image;
//            Webcam webcam = Webcam.getDefault();
//            webcam.setViewSize( new Dimension(640,480));
//            webcam.open();
//            BufferedImage buffimage = getImage();
            System.out.println("Started sending my video !");
            while(true){
//                buffimage=webcam.getImage();
                BufferedImage buffimage = getImage();
                ImageIcon imgIcon = new ImageIcon(buffimage.getScaledInstance(600,300,BufferedImage.BITMASK));
                image = buffimage.getScaledInstance(frame.getWidth(),frame.getHeight(),Image.SCALE_FAST);
                new SendVideo(imgIcon).start();
                Graphics graphics = myVideo.getGraphics();
                graphics.drawImage(image, myVideo.getWidth(), 0, -myVideo.getWidth(),myVideo.getHeight(),myVideo);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VideoCallWindow.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
       
    }   
    
    private class SendVideo extends Thread {
        ImageIcon img ;

        public SendVideo(ImageIcon arg) {
            img = arg;
        }
        
        @Override
        public void run() {
            try {
                VideoImageObject object = new VideoImageObject(img);
                oos.writeObject(object);
                oos.flush();
                oos.reset();
            } catch (IOException ex) {
                Logger.getLogger(VideoCallWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        layeredPane = new javax.swing.JLayeredPane();
        friendVideo = new javax.swing.JLabel();
        myVideo = new javax.swing.JLabel();
        videoBut = new javax.swing.JButton();
        screenShareBut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        friendVideo.setText("Waiting for video...");
        layeredPane.add(friendVideo,0);
        friendVideo.setBounds(0, 0, 580, 470);

        myVideo.setText("Waiting for video...");
        layeredPane.add(myVideo,1);
        myVideo.setBounds(400, 320, 180, 150);
        layeredPane.setLayer(myVideo, javax.swing.JLayeredPane.PALETTE_LAYER);

        videoBut.setText("Share Video");
        videoBut.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoButActionPerformed(evt);
            }
        });
        layeredPane.add(videoBut);
        videoBut.setBounds(90, 480, 140, 25);

        screenShareBut.setText("Share Screen");
        screenShareBut.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                screenShareButActionPerformed(evt);
            }
        });
        layeredPane.add(screenShareBut);
        screenShareBut.setBounds(335, 480, 129, 25);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    private void screenShareButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_screenShareButActionPerformed
        // TODO add your handling code here:
        videoBut.setEnabled(true);
        screenShareBut.setEnabled(false);
        vidOrScreen = false;
        webcam.close();
        myVideo.setVisible(false);
    }//GEN-LAST:event_screenShareButActionPerformed

    private void videoButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoButActionPerformed
        // TODO add your handling code here:
        screenShareBut.setEnabled(true);
        videoBut.setEnabled(false);
        vidOrScreen = true;
        myVideo.setVisible(true);
    }//GEN-LAST:event_videoButActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel friendVideo;
    private static javax.swing.JLayeredPane layeredPane;
    private static javax.swing.JLabel myVideo;
    private static javax.swing.JButton screenShareBut;
    private static javax.swing.JButton videoBut;
    // End of variables declaration//GEN-END:variables
}
