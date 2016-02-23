/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

/**
 *
 * @author User
 */
public class Bitmap {
    public int[][] getBitFromChunk (int layer, int[][] bytes) {
        ByteConverter bc = new ByteConverter();
        int[][] bitLayer = new int[8][8];
        return bitLayer;
    }
    
    public static void main (String[] args) {
        int layer = 1;
        int block [][] = new int [8][8];
        
        //INISIALISASI RANDOM DULU
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                block[i][j] = i*j;
            }
        }
        
        
    }
}
