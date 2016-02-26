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
    private byte[][] bytes;
    private Bitplane[] bitplanes;
    private ByteConverter byteConverter;
    
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
    
    public byte getBytesBasedOnPosition(int row, int col) {
        return bytes[row][col];
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
        Bitplane[] bitplaneResult = new Bitplane[8];
        for (int i = 0; i < bitplaneResult.length; i ++) {
            bitplaneResult[i] = new Bitplane();
        }
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                Bit[] tempBit = new Bit[8];
                tempBit = byteConverter.convertByteToBits(bytes[i][j]);
                for (int k = 0; k < bitplaneResult.length; k++) {
                    bitplaneResult[k].setBitsBasedOnPosition(i, j, tempBit[k]);
                }
            }
        }
        
        return bitplaneResult;
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
