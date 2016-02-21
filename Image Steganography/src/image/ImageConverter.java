package image;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author William Sentosa
 */
public class ImageConverter {
    private String path;
  
    public ImageConverter() {
        // do nothing
    }
    
    public ImageConverter(String path) {
        this.path = path;
    }
    
    /**
     * Setting image path
     * @param path image path
     */
    public void setImage(String path) {
        this.path = path;
    }
    
    /**
     * Convert image with the set path into bytes
     * @return bytes of image
     */
    public byte[] extractByte() {
        ByteOutputStream bos = null;
        try {
            File imgPath = new File(path);
            BufferedImage bufferedImage = ImageIO.read(imgPath);
            try {
                bos = new ByteOutputStream();
                ImageIO.write(bufferedImage, "png", bos);
            } catch (IOException ex) {
                Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    bos.close();
                } catch (Exception e) {
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bos == null ? null : bos.getBytes();
    }
    
    public BufferedImage convertToBufferedImage(byte[] bytes) {
        BufferedImage bImageFromConvert = null;
        try {
            InputStream in = new ByteArrayInputStream(bytes);
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bImageFromConvert;
    }
    
    public static void main(String args[]) {
        String path = "Mushroom.png";
        ImageConverter image = new ImageConverter(path);
        byte[] bytes = image.extractByte();
        BufferedImage buff = image.convertToBufferedImage(bytes);
        try {
            ImageIO.write(buff, "bmp", new File("output.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
