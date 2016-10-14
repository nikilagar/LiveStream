/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package share;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author nikhil
 */
public class VideoImageObject implements Serializable {
    public ImageIcon img;
    public VideoImageObject(ImageIcon tmp){
        img = tmp; 
    }
}
