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
public class Bitplane {
    // Atribut
    Bit[][] bits;
    
    // Konstruktor
    public Bitplane() {
        bits = new Bit[8][8];
    }
    
    public Bitplane(int number) {
        // Ngubah number ke bits
        bits = new Bit[8][8];
    }
    
    // Getter
    public Bit[][] getBits() {
        return bits;
    }
    
    // Setter
    public setBits(Bit[][] bits) {
        this.bits = new Bit[bits.length][bits[0].length];
        for (int i = 0; i < bits.length; i++) {
            System.arraycopy(bits[i], 0, this.bits[i], 0, bits[i].length);
        }
    }
    
    // Method
    public void convertToCGC() {
        
    }
    
    public void deconvertToPBC() {
        
    }
    
    public boolean isNoisy() {
        return true;
    }
    
    public void conjugate() {
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
