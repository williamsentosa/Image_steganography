package image;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.awt.Graphics2D;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import javax.swing.JFrame;
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
    public void getBytesFromImage() {
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

    public BufferedImage convertBytesToBufferedImage() {
        BufferedImage bImageFromConvert = null;
        try {
            byte[] byteTemp = new byte[(bytes.length)*(bytes[0].length)];
            for (int i = 0; i < bytes.length; i++) {
                for (int j = 0; j < bytes[0].length; j++) {
                    byteTemp[j + i * bytes[0].length] = bytes[i][j];
                }
            }
            
            for (int i = 0; i < byteTemp.length; i++) {
                System.out.print(byteTemp[i] + " ");
            }
            InputStream in = new ByteArrayInputStream(byteTemp);
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bImageFromConvert;
    }
    
    public Block[][] convertBytesToBlocks() {
        int blockSize = 8;
        int rows = 0;
        int byteRows = bytes[0].length;
        int byteCols = bytes.length;
        
        //defaultnya 0 jadi kalo dummy bytenya 0
        if (byteRows % blockSize != 0) {
            System.out.println(byteRows);
            byteRows = byteRows - byteRows % blockSize + blockSize;
            System.out.println(byteRows);
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
            for (int j = 0; j < byteCols/blockSize; j++) {
                blockResult[i][j] = new Block();
                byte[][] byteToBlock = new byte[8][8];
                for (int k = 0; k < blockSize; k++) {
                    for (int l = 0; l < blockSize; l++) {
                        byteToBlock[k][l] = tempBytes[k][l];
                    }
                }
                blockResult[i][j].setBytes(byteToBlock);
            }
        }
        
        return blockResult;
    }
    
    public void printSingleBlock(Block block) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(block.getBytesBasedOnPosition(i, j) + " ");
            }
            System.out.println();
        }
    }
    
    public void printBlockMatrix(Block[][] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                System.out.println((i+1)*(j+1));
                printSingleBlock(blocks[i][j]);
            }
        }
    }
    
    public void convertBlocksToBytes(Block[][] blocks) {
        int rows = blocks.length * 8;
        int cols = blocks[0].length * 8;
                
        byte[][] byteTemp = new byte[rows][cols];
        
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < 8; k++) {
                    byteTemp[i][j*8+k] = blocks[i][j].getBytesBasedOnPosition(i,k);
                }
            }
        }
        
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
   
    
    public void printBytes() {
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0 ; j < bytes[i].length; j++) {
                System.out.print(bytes[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String args[]) throws IOException {
        String path = "Mushroom.png";
        Image img = new Image(path);
        img.getBytesFromImage();
        //img.printBytes();
        
        //System.out.println(img.convertBytesToBufferedImage());
        
        img.printBlockMatrix(img.convertBytesToBlocks());
        
        
    }
    
}
