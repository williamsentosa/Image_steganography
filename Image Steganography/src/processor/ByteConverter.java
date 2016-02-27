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
    
    public Bit[][] convertIntegerToBitplane(int number) {
        //asumsi number tidak lebih dari 2 pangkat 20
        Bit[][] bitplane = new Bit[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                bitplane[i][j] = new Bit();
                bitplane[i][j].setValue(false);
            }
        }
        
        Bit[] bit = new Bit[64];
        for (int i = 0; i < bit.length; i++) {
            bit[i] = new Bit();
            bit[i].setValue(false);
        }
        
        for (int i = 30; i >= 0; i--) {
            if (number >= twoPower(i)) {
                number -= twoPower(i);
                bit[i].setValue(true);

            }
        }
        
        int counter = 63;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                bitplane[i][j] = bit[counter];
                counter--;
            }
        }
        bitplane[0][1].setValue(true); //inisialisasi untuk menentukan panjang messagenya
        
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(bitplane[i][j].getValue() + " ");
//            }
//            System.out.println();
//        }
        
        return bitplane;
    }
    
    public int convertBitplaneToInteger(Bit[][] bitplane){
        int result = 0;
        Bit[] bitTemp = new Bit[64];
        for (int i = 0; i < bitplane.length; i++) {
            for (int j = 0; j < bitplane[0].length; j++) {
                bitTemp[i*8+j] = new Bit();
                bitTemp[i*8+j].setValue(bitplane[i][j].getValue());
            }
        }
        bitTemp[1].setValue(false); // balikan inisialisasi untuk panjang pesan
        
        for (int i = 30; i >= 0; i--) {
            if (bitTemp[63-i].getValue()) {
                result+= twoPower(i);
            }
            
        }
        
        return result;
    }
    
    public static void main (String[] args) {
        ByteConverter bc = new ByteConverter();
        //System.out.println(bc.twoPower(0));
        //bc.printBitArray(bc.convertByteToBits(Byte.parseByte("-1")));
        bc.convertIntegerToBitplane(12377);
        System.out.println(bc.convertBitplaneToInteger(bc.convertIntegerToBitplane(1000)));
        //System.out.println(bc.convertBitsToByte(bc.convertByteToBits(Byte.parseByte("110"))));
        
    }
}
