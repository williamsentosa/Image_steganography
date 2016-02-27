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
        bytes = new byte[size/8];
    }
    
    public Pixel(int sizeInBit) {
        size = sizeInBit;
        bits = new Bit[size];
        bytes = new byte[size/8];
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
        ByteConverter converter = new ByteConverter();
        Bit[] temp;
        for(int i=0; i<bytes.length; i++) {
            temp = converter.convertByteToBits(bytes[i]);
            for(int j=0; j<temp.length; j++) {
                bits[i*8+j] = temp[j];
            }
        }
    }
    
    public void deconvertToPixel() {
        ByteConverter converter = new ByteConverter();
        for(int i=0; i<bytes.length; i++) {
            Bit[] temp = new Bit[8];
            for(int j=0; j<temp.length; j++) {
                temp[j] = bits[j + i*8];
            }
            bytes[i] = converter.convertBitsToByte(temp);
        }
    }
    
    public static void main(String[] args) {
        Pixel pixel = new Pixel(24);
        byte[] bytes = new byte[3];
        bytes[0] = -100;
        bytes[1] = -1;
        bytes[2] = -1;
        pixel.setBytes(bytes);
        pixel.convertToBits();
        Bit[] bits = pixel.getBits();
        for(int i=0; i<bits.length; i++) {
            System.out.print(bits[i].convertToInt());
        }
        System.out.println();
        pixel.deconvertToPixel();
        for(int i=0; i<pixel.getBytes().length; i++) {
            System.out.print(pixel.getBytes()[i] + " ");
        }
        System.out.println();
    }
    
    public void printPixel() {
        System.out.println(size);
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(bytes[i] + " ");
        }
    }
    
}
