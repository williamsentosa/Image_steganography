package processor;

/**
 *
 * @author William Sentosa - 13513026
 * @author Candy Olivia Mawalim - 13513031
 * @author Angela Lynn - 13513032
 */
public class ConjugationTable {
    private Bitplane[] bitplanes;
    private int size;
    private int index,x,y;
    
    public ConjugationTable() {
        size = 0;
        index = 0;
        x = 0;
        y = 0;
    }
    
    /**
     * Constructor with message size as input
     * @param msgSize the size of message in byte
     */
    public ConjugationTable(int msgSize) {
        int countBitplane;
        
        if((msgSize % 64) == 0) {
            countBitplane = (msgSize / 64) * 8; 
        } else {
            countBitplane = ((msgSize / 64) + 1) * 8;
        }
        if ((countBitplane % 64) == 0) {
            size = countBitplane / 64;
        } else {
            size = countBitplane / 64 + 1;
        }
        bitplanes = new Bitplane[size];
        for (int i = 0; i < bitplanes.length; i++) {
            bitplanes[i] = new Bitplane();
        }
        initiateTable();
        index = 0;
        x = 0;
        y = 0;
    }
    
    // Setter
    public void setBitplanes(Bitplane[] bitplanes) {
        this.bitplanes = bitplanes;
        size = bitplanes.length;
    }
    
    public void setSize(int size) {
        this.size = size;
        bitplanes = new Bitplane[size];
        for (int i = 0; i < bitplanes.length; i++) {
            bitplanes[i] = new Bitplane();
        }
        initiateTable();
    }
    
    // Getter
    public Bitplane[] getBitplanes() {
        return bitplanes;
    }
    
    public int getSize() {
        return size;
    }
    
    // Method
    /**
     * add bit to bitplanes
     * @param bit added bit
     */
    public void addSign(Bit bit) {
        bitplanes[index].setBitsBasedOnPosition(x, y, bit);
        if (y == 7) {
            y = 0;
            if (x == 7) {
                index++;
                x = 0;
            } else {
                x++;
            }
        } else {
            y++;
        }
    }
    
    public Bit[] getConjugationArray(int msgSize) {
        Bit[] bits;
        int countBitplane, bitplaneIndex = 0, row = 0, column = 0;
        
        if((msgSize % 64) == 0) {
            countBitplane = (msgSize / 64) * 8; 
        } else {
            countBitplane = ((msgSize / 64) + 1) * 8;
        }
        bits = new Bit[countBitplane];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = bitplanes[bitplaneIndex].getBitsBasedOnPosition(row, column);
            if (column == 7) {
                column = 0;
                if (row == 7) {
                    bitplaneIndex++;
                    row = 0;
                } else {
                    row++;
                }
            } else {
                column++;
            }
        }
        
        return bits;
    }
    
    private void initiateTable() {
        for (int i = 0; i < bitplanes.length; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    bitplanes[i].setBitsBasedOnPosition(j, k, new Bit());
                }
            }
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Bit[] bits;
        int countBitplane, msgSize = 550;
        ConjugationTable table = new ConjugationTable(msgSize);
        
        if((msgSize % 64) == 0) {
            countBitplane = (msgSize / 64) * 8; 
        } else {
            countBitplane = ((msgSize / 64) + 1) * 8;
        }
        for (int i = 0; i < countBitplane; i++) {
            table.addSign(new Bit(true));
        }
        System.out.println("Count Bitplane " + countBitplane);
        System.out.println("Count Table " + table.getSize());
        bits = table.getConjugationArray(msgSize);
        System.out.println("Conjugation Array");
        for (int i = 0; i < bits.length; i++) {
            System.out.print(bits[i].convertToInt() + " ");
        }
        System.out.println("\n");
        for (int i = 0; i < table.getSize(); i++) {
            System.out.println("Conjugation Table " + i);
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    System.out.print(table.getBitplanes()[i].getBitsBasedOnPosition(j, k).convertToInt() + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
