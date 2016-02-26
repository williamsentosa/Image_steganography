/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

/**
 *
 * @author William Sentosa
 */
public class Pixel {
    private int size; // In bit
    private Bit[] bits;
    private byte[] bytes;
    
    public Pixel() {
        size = 0;
        bits = new Bit[size];
    }
    
    public Pixel(int sizeInBit) {
        size = sizeInBit;
        bits = new Bit[size];
    }
    
    public void setSize(int sizeInBit) {
        size = sizeInBit;
    }
    
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    
    public int getSize() {
        return size;
    }
    
    public byte[] getBytes() {
        return bytes;
    }
    
    public Bit[] getBits() {
        return bits;
    }
    
    public void setBits(Bit[] bits) {
        this.bits = bits;
    }
    
    public void convertToBits() {
        int numByte = size/8;
        
    }
    
    public void deconvertToPixel() {
        
    }
    
}
