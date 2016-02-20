/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image;

/**
 *
 * @author Candy Olivia Mawalim
 */
public class ImageQuality {
    public double mean(int[] numbers) {
        double sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        return ((double)sum/(double)(numbers.length));
    }
    
    public double countRootMeanSquare(int[] numbers) {
        double res = 0;
        //ini RMS beda dengan yang biasa :(
        return res;
    }
    
}
