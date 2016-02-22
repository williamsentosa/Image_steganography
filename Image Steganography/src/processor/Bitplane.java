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
    private Bit[][] bits;
    private final int size = 8;
    
    // Konstruktor
    public Bitplane() {
        bits = new Bit[size][size];
    }
    
    public Bitplane(int number) {
        // Ngubah number ke bits
        bits = new Bit[size][size];
    }
    
    // Getter
    public Bit[][] getBits() {
        return bits;
    }
    
    // Setter
    public void setBits(Bit[][] bits) {
        this.bits = bits;
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

    public static void main(String[] args) {
        
    }
    
}
