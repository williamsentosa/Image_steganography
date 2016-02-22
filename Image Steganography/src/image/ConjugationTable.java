package image;

/**
 *
 * @author William Sentosa
 */
public class ConjugationTable {
    private Bitplane[] bitplanes;
    private int size;
    private int x,y;
    
    public ConjugationTable() {
        size = 0;
        x = 0;
        y = 0;
    }
    
    /**
     * Constructor with message size as input
     * @param msgSize the size of message in byte
     */
    public ConjugationTable(int msgSize) {
        if(msgSize/8 % 8 == 0) {
            size = msgSize/8; 
        } else {
            size = msgSize/8 + 1;
        }
        Bitplanes = new Bitplane[size];
        x = 0;
        y = 0;
    }
    
    public setBitplanes(Bitplane[] bitplanes) {
        this.bitplanes = bitplanes;
    }
    
    /**
     * add bit to bitplanes
     * @param bit added bit
     */
    public addSign(Bit bit) {
        
    }
    
    public Bitplane[] getBitplanes() {
        return bitplanes;
    }
    
    
}
