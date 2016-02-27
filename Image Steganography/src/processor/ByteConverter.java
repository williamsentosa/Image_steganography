/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

import java.util.BitSet;

/**
 *
 * @author Candy Olivia Mawalim
 */
public class ByteConverter {
    public int twoPower (int i) {
        int res = 1;
        for (int j = 0; j < i; j++) {
            res *= 2;
        }
        return res;
    }
    
    public byte convertBitsToByte(Bit[] bits) {
        byte result = 0;
        for (int i = 1; i <= bits.length ; i++) {
            if (bits[i-1].getValue() == true) {
                result += twoPower(8-i);
            }
        }
        return result;
    }
    
    public Bit[] convertByteToBits(byte bytee) {
        Bit[] bits = new Bit[8];
        int b = (int) bytee; 
        
        for (int i = 0; i < bits.length; i++) {
            bits[i] = new Bit();
        }
         
        if (b < 0) {
            b += 256;
            System.out.println(b);
        }
           
        for (int i = 1; i <= 8; i++) {
            if (b >= twoPower(8-i)) {
                bits[i-1].setValue(true);
                b -= twoPower(8-i);
                //System.out.print(twoPower(8-i) + " ");        
            }
            if (b == 0) 
                break;
        }
        //System.out.println();
        
        return bits;
    }
    
    public void printBitArray(Bit[] bits) {
        for (int i = 0; i < 8; i++) {
            System.out.print(bits[i].getValue()+" ");
        }
        System.out.println();
    }
    
    public static void main (String[] args) {
        ByteConverter bc = new ByteConverter();
        //System.out.println(bc.twoPower(0));
        bc.printBitArray(bc.convertByteToBits(Byte.parseByte("-1")));
        //System.out.println(bc.convertBitsToByte(bc.convertByteToBits(Byte.parseByte("110"))));
        
    }
}
