/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

import image.Image;

/**
 *
 * @author Candy Olivia Mawalim
 */
public class Block {
    // Atribut
    private Pixel[][] pixels;
    private Bitplane[] bitplanes;
    private ByteConverter byteConverter;
    private final int size = 8;
    
    // Konstruktor
    public Block() {
        pixels = new Pixel[size][size];
        bitplanes = new Bitplane[0];
    }
    
    // Getter
    public Pixel[][] getPixels() {
        return pixels;
    }
    
    public Bitplane[] getBitplanes() {
        return bitplanes;
    }
    
    // Setter
    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
    }
    
    public void setBitplanes(Bitplane[] bitplanes) {
        this.bitplanes = bitplanes;
    }
    
    // Method
    public void convertToBitplanes() {
        int size = pixels[0][0].getSize();
        Bitplane[] bitplaneResult = new Bitplane[size];
        Bit[][][] bitsTemp = new Bit[this.size][this.size][size];
        for (int i = 0; i < pixels.length; i ++) {
            for(int j=0; j < pixels[i].length; j++) {
                bitsTemp[i][j] = pixels[i][j].getBits();
            }
        }
        for(int k=0; k < size; k++) {
            Bit[][] bitTemp = new Bit[this.size][this.size];
            for(int i=0; i < bitsTemp.length; i++) {
                for(int j=0; j<bitsTemp[i].length; j++) {
                    bitTemp[i][j] = bitsTemp[i][j][k];
                }
            }
            bitplaneResult[k] = new Bitplane();
            bitplaneResult[k].setBits(bitTemp);
        }
    }
    
    public void deconvertFromBitplanes() {
        int pixelSize = bitplanes.length;
        Bit[][][] bitsTemp = new Bit[this.size][this.size][pixelSize];
        for(int k=0; k < pixelSize; k++) {
            Bit[][] bitTemp = this.bitplanes[k].getBits();
            for(int i=0; i < bitsTemp.length; i++) {
                for(int j=0; j<bitsTemp[i].length; j++) {
                    bitsTemp[i][j][k] = bitTemp[i][j];
                }
            }
        }
        for (int i = 0; i < bitsTemp.length; i ++) {
            for(int j=0; j < bitsTemp[i].length; j++) {
                pixels[i][j].setBits(bitsTemp[i][j]);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String path = "grayscale.png";
        Image image = new Image(path);
        image.convertImageToPixels();
        image.convertPixelsToBlocks();
        int size = 8;
        Block block = new Block();
        Pixel[][] pixels = new Pixel[size][size];
        
        
    }
    
}
