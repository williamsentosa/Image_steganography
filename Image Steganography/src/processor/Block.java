/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

import image.Image;

/**
 *
 * @author William Sentosa - 13513026
 * @author Candy Olivia Mawalim - 13513031
 * @author Angela Lynn - 13513032
 */
public class Block {
    // Atribut
    private Pixel[][] pixels;
    private Bitplane[] bitplanes;
    private final int size = 8;
    
    // Konstruktor
    public Block() {
        pixels = new Pixel[size][size];
        bitplanes = new Bitplane[0];
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                pixels[i][j] = new Pixel();
            }
        }
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
        int pixelSize = pixels[0][0].getSize();
        Bitplane[] bitplaneResult = new Bitplane[pixelSize];
        Bit[][][] bitsTemp = new Bit[this.size][this.size][size];
        for (int i = 0; i < pixels.length; i ++) {
            for(int j = 0; j < pixels[i].length; j++) {
                pixels[i][j].convertToBits();
                Bit[] bits = pixels[i][j].getBits();
                bitsTemp[i][j] = pixels[i][j].getBits();
            }
        }
        for(int k=0; k < pixelSize; k++) {
            Bit[][] bitTemp = new Bit[this.size][this.size];
            for(int i=0; i < bitsTemp.length; i++) {
                for(int j=0; j<bitsTemp[i].length; j++) {
                    bitTemp[i][j] = bitsTemp[i][j][k];
                }
            }
            bitplaneResult[k] = new Bitplane();
            bitplaneResult[k].setBits(bitTemp);
        }
        
        this.bitplanes = bitplaneResult;
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
            for(int j = 0; j < bitsTemp[i].length; j++) {
                pixels[i][j].setBits(bitsTemp[i][j]);
                pixels[i][j].deconvertToPixel();
            }
        }
    }
    
    public String toString() {
        String result = "";
        for(int i=0; i< pixels.length; i++) {
            for(int j=0; j<pixels[i].length; j++) {
                result = result + pixels[i][j] + ",";
            }
            result = result + "\n";
        }
        return result;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String path = "flower.png";
        Image image = new Image(path);
        Block[][] blocks = image.convertImageToBlocks();
        blocks[20][5].getPixels()[0][0].printPixel();
        
        blocks[20][5].convertToBitplanes();
        Bitplane[] bitplanes = blocks[20][5].getBitplanes();
        for(int i=0; i<bitplanes.length; i++) {
            for(int j=0; j<bitplanes[i].getBits().length; j++) {
                for(int k=0; k<bitplanes[i].getBits()[j].length; k++) {
                    Bit bit = bitplanes[i].getBits()[j][k];
                    System.out.print(bit.convertToInt());
                }
                System.out.println();
            }
            System.out.println("************************************************");
        }
        blocks[20][5].deconvertFromBitplanes();
        blocks[20][5].getPixels()[0][0].printPixel();
        
    }
    
}
