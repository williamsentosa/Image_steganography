package image;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import sun.misc.BASE64Decoder;

/**
 *
 * @author William Sentosa
 */
public class Image {
    private String path;

    public Image() {
        // do nothing
    }
    
    public Image(String path) {
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
                Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    bos.close();
                } catch (Exception e) {
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bos == null ? null : bos.getBytes();
    }
    
    /**
     * Convert image with the set path into Base64
     * @return encodedBase64String
     */
    public String encodedBase64 () {
        String encodedImage = "";
        try {
            BufferedImage bufImage;
            bufImage = ImageIO.read(new File(path));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufImage, "png", baos);
            encodedImage = Base64.encode(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedImage;
    }
    
    /**
     * Convert Base64 String into Image
     * @param encodedBased64String, resultPath
     */
    public void base64ToImage (String encodedString, String resultPath) throws IOException {
        byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(encodedString);
        File of = new File(resultPath);
        FileOutputStream osf = new FileOutputStream(of);
        osf.write(btDataFile);
        osf.flush();
    }
    
    public static void main(String args[]) throws IOException {
        String path = "Mushroom.PNG";
        Image image = new Image(path);
        //byte[] bytes = image.extractByte();
        //System.out.println(Arrays.toString(bytes));
        String encodedString = image.encodedBase64();
        System.out.println(image.encodedBase64());
        
        image.base64ToImage(encodedString, "Mushroom_res.png");
    }
    
}
