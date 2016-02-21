/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image;

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
    
    public int[] byteToBit(int b) {
        int[] bit = new int[8];
        if (b < 0) {
            b += 256;
        }
           
        for (int i = 1; i <= 8; i++) {
            if (b >= twoPower(8-i)) {
                bit[i-1] = 1;
                b -= twoPower(8-i);
                //System.out.print(twoPower(8-i) + " ");
                
            }
            if (b == 0) 
                break;
        }
        //System.out.println();
        
        return bit;
    }
    
    public int bitToByte(int[] bit) {
        int res = 0;
        for (int i = 1; i <= bit.length ; i++) {
            if (bit[i-1] == 1) {
                res += twoPower(8-i); 
                //System.out.print(twoPower(8-i)+ " ");
            }
        }
        //System.out.println();
        return res;
    }
    
    public void printBitArray(int[] bit) {
        for (int i = 0; i < 8; i++) {
            System.out.print(bit[i]);
        }
        System.out.println();
    }
    
    public static void main (String[] args) {
        ByteConverter bc = new ByteConverter();
        System.out.println(bc.twoPower(0));
        bc.printBitArray(bc.byteToBit(110));
        System.out.println(bc.bitToByte(bc.byteToBit(110)));
        
    }
}
