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
import java.awt.Graphics2D;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import processor.Block;

import processor.ByteConverter;


/**
 *
 * @author William Sentosa
 */
public class Image {
    // Atribut
    private byte[][] bytes;
    private String path;

    // Konstruktor
    public Image() {
        // do nothing
    }
    
    public Image(String path) {
        this.path = path;
    }
    
    // Getter
    public byte[][] getBytes() {
        return bytes;
    }
    
    public String getPath() {
        return path;
    }
    
    // Setter
    public void setBytes(byte[][] bytes) {
        this.bytes = new byte[bytes.length][bytes[0].length];
        for (int i = 0; i < bytes.length; i++) {
            System.arraycopy(bytes[i], 0, this.bytes[i], 0, bytes[i].length);
        }
    }
    
    /**
     * Setting image path
     * @param path image path
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    // Method
    public void getBytesFromImage(String path) {
        // Masukkin ke bytes
        try {
            File imgPath = new File(path);
            BufferedImage bufferedImage = ImageIO.read(imgPath);
            
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
            byte[] dataByte = data.getData();
            int rows = bufferedImage.getWidth();
            int cols = bufferedImage.getHeight();
            bytes = new byte[rows][cols];
            for(int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    bytes[i][j] = dataByte[j+i*cols];
                }
            }
           
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public BufferedImage convertBytesToBufferedImage(byte[][] bytes) {
        BufferedImage bImageFromConvert = null;
        try {
            byte[] byteTemp = new byte[(bytes.length)*(bytes[0].length)];
            for (int i = 0; i < bytes.length; i++) {
                for (int j = 0; j < bytes[0].length; j++) {
                    byteTemp[j + i * bytes[0].length] = bytes[i][j];
                }
            }
            InputStream in = new ByteArrayInputStream(byteTemp);
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bImageFromConvert;
    }
    
    public Block[][] convertBytesToBlock() {
        int blockSize = 8;
        int rows = 0;
        int byteRows = bytes[0].length;
        int byteCols = bytes.length;
        
        if (byteRows % blockSize != 0) {
            byteRows = byteRows - byteRows % blockSize + blockSize;
        } 
        
        if (byteCols % blockSize != 0) {
            byteCols = byteCols - byteCols % blockSize + blockSize;
        } 
        
        //Inisialisasi tempBytes
        byte[][] tempBytes = new byte[byteRows][byteCols];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[0].length; j++) {
                tempBytes[i][j] = bytes[i][j];
            }
        }
        
        Block[][] blockResult = new Block[byteRows/blockSize][byteCols/blockSize];
        for (int i = 0; i < byteRows/blockSize; i++) {
            for(int j = 0; j < byteCols/blockSize; j++) {
                blockResult[i][j] = new Block();
                for (int k = 0; k < blockSize; k++) {
                    for (int l = 0; l < blockSize; l++) {
                        
                    }
                }
            }
        }
        
        return blockResult;
    }
    
    //COBA-COBA
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
    
    
    
    public void splitImage(BufferedImage img) throws IOException {
        int chunksWidth = 8;
        int chunksHeight = 8;
        int rows = img.getWidth() / chunksHeight;
        int cols = img.getHeight() / chunksWidth;
        int chunks = rows * cols;
        
        int count = 0;
        
        BufferedImage images[] = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {  
            for (int y = 0; y < cols; y++) {  
                //Initialize the image array with image chunks  
                images[count] = new BufferedImage(chunksWidth, chunksHeight, img.getType());  
  
                // draws the image chunk  
                Graphics2D gr = images[count++].createGraphics();  
                gr.drawImage(img, 0, 0, chunksWidth, chunksHeight, chunksWidth * y, chunksHeight * x, chunksWidth * y + chunksWidth, chunksHeight * x + chunksHeight, null);  
                gr.dispose();  
            }  
        }  
        System.out.println("Splitting done" + images.length); 
        
        //Write mini images to image file
       /*
        for (int i = 0; i < images.length; i++) {  
            ImageIO.write(images[i], "jpg", new File("img" + i + ".jpg"));  
        }  
        System.out.println("Mini images created");  */
    }
    
    public Block[][] convertToBlocks() {
        return new Block[8][8];
    }
    
    public void deconvertFromBlocks(Block[][] blocks) {
        
    }
    
    public double checkImageQuality(Image comparisonImage) {
        return 0;
    }
    
    private double countRootMeanSquare(Byte[][] coverImageBytes, Byte[][] stegoImageBytes) {
        double rms = 0, sum = 0;
        
        for (int i = 0; i < coverImageBytes.length; i++) {
            for (int j = 0; j < coverImageBytes[i].length; j++) {
                sum = sum + Math.pow(coverImageBytes[i][j] - stegoImageBytes[i][j], 2);
            }
        }
        
        rms = Math.sqrt(sum / ((double) coverImageBytes.length * (double) coverImageBytes[0].length));
        
        return rms;
    }
    
    public static void main(String args[]) throws IOException {
        String path = "Mushroom.png";
        Image image = new Image(path);
        byte[] bytes = image.extractByte();
        System.out.println(Arrays.toString(bytes));
        ByteConverter bc = new ByteConverter();
        bc.printBitArray(bc.convertByteToBits(Byte.parseByte("-119")));
        
        char b = '0';
//        System.out.println("result : " + c | b);

        //image.splitImage(ImageIO.read(new File(path)));
        /*String encodedString = image.encodedBase64();
        encodedString = encodedString;
        System.out.println(image.encodedBase64());
        
        image.base64ToImage(encodedString, "Mushroom_res.png");*/
    }
    
}
