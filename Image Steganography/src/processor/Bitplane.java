/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

/**
 *
 * @author William Sentosa - 13513026
 * @author Candy Olivia Mawalim - 13513031
 * @author Angela Lynn - 13513032
 */
public class Bitplane {
    // Atribut
    private Bit[][] bits;
    private final int size = 8;
 
    // Konstruktor
    public Bitplane() {
        bits = new Bit[size][size];
    }
    
    // Getter
    public Bit[][] getBits() {
        return bits;
    }
    
    public Bit getBitsBasedOnPosition (int row, int col) {
        return bits[row][col];
    }
    
    // Setter
    public void setBits(Bit[][] bits) {
        this.bits = bits;
    }
    
    public void setBitsBasedOnPosition (int col, int row, Bit bit) {
        this.bits[col][row] = bit;
    }
    
    public int getSize() {
        return size;
    }
    
    private Bit[][] copyBits(Bit[][] src) {
        Bit[][] result = new Bit[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                result[i][j] = new Bit(src[i][j].getValue());
            }
        }
        return result;
    }
    
    // Method
    public int getBitColumn() {
        return bits.length;
    }
    
    public int getBitRow() {
        return bits[0].length;
    }
            
    public void convertToCGC() {
        Bit[][] temp = copyBits(bits);
        for(int i=1; i<size; i++) {
            for(int j=0; j<size; j++) {
                bits[j][i].setValue(temp[j][i-1].getValue() ^ temp[j][i].getValue());
            }
        }
    }
    
    public void deconvertToPBC() {
        Bit[][] temp = copyBits(bits);
        for(int i=1; i<size; i++) {
            for(int j=0; j<size; j++) {
                bits[j][i].setValue(bits[j][i-1].getValue() ^ temp[j][i].getValue());
            }
        }
    }
    
    public boolean isNoisy(double threshold) {
        int countChangeColorsFrequency = 0;
        int maxFrequency = 112;
        for(int i=0; i<size; i++) {
            for(int j=0; j<size-1; j++) {
                if(!(bits[i][j].isSameAs(bits[i][j+1]))) {
                    countChangeColorsFrequency++;
                }
            }
        }
        for(int i=0; i<size; i++) {
            for(int j=0; j<size-1; j++) {
                if(!(bits[j][i].isSameAs(bits[j+1][i]))) {
                    countChangeColorsFrequency++;
                }
            }
        }
        return (double)countChangeColorsFrequency/(double)maxFrequency > threshold;
    }
    
    public double checkNoisy() {
        int countChangeColorsFrequency = 0;
        int maxFrequency = 112;
        for(int i=0; i<size; i++) {
            for(int j=0; j<size-1; j++) {
                if(!(bits[i][j].isSameAs(bits[i][j+1]))) {
                    countChangeColorsFrequency++;
                }
            }
        }
        for(int i=0; i<size; i++) {
            for(int j=0; j<size-1; j++) {
                if(!(bits[j][i].isSameAs(bits[j+1][i]))) {
                    countChangeColorsFrequency++;
                }
            }
        }
        return (double)countChangeColorsFrequency/(double)maxFrequency;
    }
    
    private Bit[][] makeWcPattern() {
        Bit[][] result = new Bit[8][8];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                Bit b = new Bit();
                b.setValue((i+j)%2);
                result[i][j] = b;
            }
        }
        return result;
    }
    
    public boolean isConjugated() {
        return !bits[0][1].getValue();
    }
    
    public void conjugate() {
        Bit[][] wc = makeWcPattern();
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                bits[i][j].setValue(bits[i][j].getValue() ^ wc[i][j].getValue());
            }
        }
    }
    
    public String toString() {
        String result = "";
        for(int i=0; i<bits.length; i++) {
            for(int j=0; j<bits[i].length; j++) {
                result = result + bits[i][j].convertToInt() + " ";
            }
            result = result + "\n";
        }
        return result;
    }
    
    public static void main(String[] args) {
        Bitplane bitplane = new Bitplane();
        Bit[][] wc = bitplane.makeWcPattern();
        bitplane.setBits(wc);
        System.out.println("Value : " + bitplane.checkNoisy());
        bitplane.conjugate();
        System.out.println("Value : " + bitplane.checkNoisy());
    }
    
}
