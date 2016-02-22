/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

/**
 *
 * @author angelynz95
 */
public class Block {
    // Atribut
    private byte[][] bytes;
    private Bitplane[] bitplanes;
    
    // Konstruktor
    public Block() {
        
    }
    
    // Getter
    public byte[][] getBytes() {
        return bytes;
    }
    
    public Bitplane[] getBitplanes() {
        return bitplanes;
    }
    
    // Setter
    public void setBytes(byte[][] bytes) {
        this.bytes = new byte[bytes.length][bytes[0].length];
        for (int i = 0; i < bytes.length; i++) {
            System.arraycopy(bytes[i], 0, this.bytes[i], 0, bytes[i].length);
        }
    }
    
    public void setBitplanes(Bitplane[] bitplanes) {
        this.bitplanes = new Bitplane[bitplanes.length];
        System.arraycopy(bitplanes, 0, this.bitplanes, 0, bitplanes.length);
    }
    
    // Method
    public Bitplane[] convertToBitplanes() {
        return new Bitplane[0];
    }
    
    public void deconvertFromBitplanes(Bitplane[] bitplanes) {
        
    }
    
    private byte convertBitsToByte(Bit[] bits) {
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
