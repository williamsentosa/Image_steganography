package image;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import javax.swing.JFrame;
import processor.Block;

import processor.ByteConverter;
import processor.Pixel;


/**
 *
 * @author William Sentosa
 */
public class Image {
    // Atribut
    private String path;
    private int pixelSize;
    private Pixel[][] pixels;
            
    // Konstruktor
    public Image() {
        // do nothing
    }
    
    public Image(String path) {
        this.path = path;
    }
    
    // Getter
    public Pixel[][] getPixel() {
        return pixels;
    }
    
    public String getPath() {
        return path;
    }

    public int getPixelSize() {
        return pixelSize;
    }
    
    // Setter
    public void setPixels(Pixel[][] pixels) {
        this.pixels = new Pixel[pixels.length][pixels[0].length];
        for (int i = 0; i < pixels.length; i++) {
            System.arraycopy(pixels[i], 0, this.pixels[i], 0, pixels[i].length);
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
    private byte[] getBytesFromImage(BufferedImage bufferedImage) {
        byte[] dataByte = null;
        
        pixelSize = bufferedImage.getColorModel().getPixelSize();
        System.out.println(pixelSize);
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        dataByte = data.getData();
        /*
        for (int i = 0; i < dataByte.length; i++) {
            System.out.print(dataByte[i] + " ");
        }
        System.out.println();
        */
        return dataByte;
    }
    
    //Convert Image to Pixels untuk yang origin
    public void convertImageToPixels() {
        // Masukkin ke Pixels
        try {
            File imgPath = new File(path);
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(imgPath);
            
            byte[] dataByte = getBytesFromImage(bufferedImage);
            
            int cols = bufferedImage.getHeight();
            int rows = bufferedImage.getWidth();
            pixels = new Pixel[cols][rows];
            
            //System.out.println(dataByte.length);
            int idx = 0;
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    pixels[i][j] = new Pixel(pixelSize);
                    byte[] byteTemp = new byte[pixelSize/8];
                    for (int k = 0; k < pixelSize/8; k++) {
                        byteTemp[k] = dataByte[idx + k+(j*(pixelSize/8))+(i*rows*pixelSize/8)];
                        //System.out.println(idx + k+(j*(pixelSize/8))+(i*cols*pixelSize/8));
                    }
                    pixels[i][j].setBytes(byteTemp);
                }
            }  
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Untuk gambar bebas
    public Pixel[][] convertImagesToPixels(String path) {
        // Masukkin ke Pixels
        Pixel[][] pixels = null;
        try {
            File imgPath = new File(path);
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(imgPath);
            
            byte[] dataByte = getBytesFromImage(bufferedImage);
            
            int cols = bufferedImage.getHeight();
            int rows = bufferedImage.getWidth();
            pixels = new Pixel[cols][rows];
            
            System.out.println(dataByte.length);
            int idx = 0;
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    pixels[i][j] = new Pixel(pixelSize);
                    byte[] byteTemp = new byte[pixelSize/8];
                    for (int k = 0; k < pixelSize/8; k++) {
                        byteTemp[k] = dataByte[idx + k+(j*(pixelSize/8))+(i*rows*pixelSize/8)];
                        //System.out.println(idx + k+(j*(pixelSize/8))+(i*cols*pixelSize/8));
                    }
                    pixels[i][j].setBytes(byteTemp);
                }
            }  
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pixels;
    }

    private byte[] convertPixelsToBytes() {
        int cols = pixels.length;
        int rows = pixels[0].length;
        
        byte[] resultByte = new byte[rows*cols*pixelSize/8];
        
        System.out.println(resultByte.length);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < pixelSize/8; k++) {
                    //System.out.println((k+j*(pixelSize/8)+i*cols*pixelSize/8) + " " + pixels[i][j].getBytes()[k]);
                    resultByte[k+j*(pixelSize/8)+i*rows*pixelSize/8] = pixels[i][j].getBytes()[k];
                }
                
            }
        }
        /*
        for (int i = 0; i < resultByte.length; i++) {
            System.out.print(resultByte[i] + " ");
        }
        System.out.println();
        
        System.out.println(rows + " " + cols);*/
        return resultByte;
    }
    
    public BufferedImage convertPixelsToBufferedImage() {
        BufferedImage image = null;
        try {
            byte[] dataByte = convertPixelsToBytes();
            int cols = pixels.length;
            int rows = pixels[0].length;
            
            switch (pixelSize) {
                case 8:  
                    image = new BufferedImage(rows, cols, BufferedImage.TYPE_BYTE_GRAY);
                    break;
                case 24:  
                    image = new BufferedImage(rows, cols, BufferedImage.TYPE_3BYTE_BGR);
                    break;
                case 32:  
                    image = new BufferedImage(rows, cols, BufferedImage.TYPE_4BYTE_ABGR);
                    break;
            }
            
            image.setData(Raster.createRaster(image.getSampleModel(), new DataBufferByte(dataByte, dataByte.length), new Point()));
            ImageIO.write(image,"png",new File("new1.png"));
            
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    private Block convertPixelsToBlock() {
        int col = pixels.length;
        int row = pixels[0].length;
        
        //System.out.println(row + " " + col);
        
        if (row % 8 != 0) {
            row = 8 + (row/8)*8;
        } 
        
        if (col % 8 != 0) {
            col = 8 + (col/8)*8;
        } 
        
        //System.out.println(row + " " + col);
        Block block = new Block();
        Pixel[][] blockPixel = new Pixel[col][row];
        
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                //System.out.println(i + " " + j);
                blockPixel[i][j] = new Pixel(pixelSize);
                
                for (int k = 0; k < pixelSize/8; k++) {
                    blockPixel[i][j].getBytes()[k] = (byte)0;
                }
            }
        }
        
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                //System.out.println(i + " " + j);
                blockPixel[i][j] = pixels[i][j];
            }
        }
        block.setPixels(blockPixel);
        
        return block;
    }
    
    public Block[][] convertPixelsToBlocks() {
        Block bigBlock = convertPixelsToBlock();
        int col = bigBlock.getPixels()[0].length/8;
        int row = bigBlock.getPixels().length/8;
        Block[][] blocks = new Block[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                blocks[i][j] = new Block();
                for (int l = 0; l < 8; l++) {
                    for (int k = 0; k < 8; k++) {
                        blocks[i][j].getPixels()[l][k] = bigBlock.getPixels()[l+i*8][k+j*8];
                        //System.out.println("blok : " + i + "," + j + " pixel ke : " + l + "," + k + " bigblock pixel ke : " + (l+i*8) + "," + (k+j*8));
                    }
                }
                
            }
        }
        
        return blocks;
    }
    
    public Block[][] convertImageToBlocks() {
        convertImageToPixels();
        convertPixelsToBlock();
        return convertPixelsToBlocks();
    }
    
    public BufferedImage convertPixelMatrixToBufferedImage(Pixel[][] pixels) {
        BufferedImage image = null;
        try {
            int cols = pixels.length;
            int rows = pixels[0].length;
            
            byte[] dataByte = new byte[rows*cols*pixelSize/8];
        
            //System.out.println(dataByte.length);
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    for (int k = 0; k < pixelSize/8; k++) {
                        //System.out.println((k+j*(pixelSize/8)+i*cols*pixelSize/8) + " " + pixels[i][j].getBytes()[k]);
                        dataByte[k+j*(pixelSize/8)+i*rows*pixelSize/8] = pixels[i][j].getBytes()[k];
                    }

                }
            }
            
            switch (pixelSize) {
                case 8:  
                    image = new BufferedImage(rows, cols, BufferedImage.TYPE_BYTE_GRAY);
                    break;
                case 24:  
                    image = new BufferedImage(rows, cols, BufferedImage.TYPE_3BYTE_BGR);
                    break;
                case 32:  
                    image = new BufferedImage(rows, cols, BufferedImage.TYPE_4BYTE_ABGR);
                    break;
            }
            
            image.setData(Raster.createRaster(image.getSampleModel(), new DataBufferByte(dataByte, dataByte.length), new Point()));
            ImageIO.write(image,"png",new File("newcoba.png"));
            
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    public double countRMS(Pixel[][] pixOrigin, Pixel[][] pixStegano) {
        double res;
        int col = pixOrigin.length;
        int row = pixOrigin[0].length;
        double total = (double)(col*row);
        
        double sum = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < pixOrigin[0][0].getSize()/8; k++) {
                    System.out.println(pixOrigin[i][j].getBytes()[k] + " " + pixStegano[i][j].getBytes()[k]);
                    
                    sum += Math.pow((pixOrigin[i][j].getBytes()[k]-pixStegano[i][j].getBytes()[k]),2);
                }
            }
        }
        res = Math.sqrt(sum/total);
        return res;
    }
    
    public void checkImageQuality () {
        
    }
    
    public static void main(String args[]) throws IOException {
        String path = "mushroom.png";
        Image img = new Image(path);
        img.convertImageToPixels();
        img.convertPixelsToBufferedImage();
        //img.convertPixelsToBlocks();
        //img.convertPixelMatrixToBufferedImage(img.convertPixelsToBlocks().getPixels());
        //img.convertImageToPixel();
        
        Pixel[][] pixel = img.convertImagesToPixels("newcoba.png");
        for (int i = 0; i < 80; i++) {
            //System.out.print(pixels[i][0].getBytes()[1] + " ");
            pixel[i][0].getBytes()[1] = (byte) 100;
            //System.out.println(pixels[i][0].getBytes()[1]);
        }
        
        
       System.out.println(img.countRMS(img.getPixel(), pixel));
        
    }
    
}
