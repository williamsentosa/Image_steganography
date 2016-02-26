/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

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
        for (int i = 0; i < bitplaneResult.length; i ++) {
            bitplaneResult[i] = new Bitplane();
        }
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                Bit[] tempBit = new Bit[8];
                //tempBit = byteConverter.convertByteToBits(pixels[i][j]);
                for (int k = 0; k < bitplaneResult.length; k++) {
                    bitplaneResult[k].setBitsBasedOnPosition(i, j, tempBit[k]);
                }
            }
        }
        
    }
    
    public byte[][] deconvertFromBitplanes(Bitplane[] bitplanes) {
        int row = bitplanes[0].getBitRow();
        int col = bitplanes[0].getBitColumn();
        byte[][] bytesResult = new byte[row][col];
        Bit[] bitTemp = new Bit[8];
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                for (int k = 0; k < 8; k++) {
                    bitTemp[k] = bitplanes[k].getBitsBasedOnPosition(i, j);
                }
                bytesResult[i][j] = byteConverter.convertBitsToByte(bitTemp);
            }
        }
        
        return bytesResult;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Block block = new Block();
        Bit[] bits = new Bit[8];
        
        
        
    }
    
}
