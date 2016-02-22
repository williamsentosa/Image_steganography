package image;

/**
 *
 * @author William Sentosa
 */
public class Bit {
    private boolean value;
    
    public Bit() {
        value = false;
    }
    
    public Bit(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    public void setValue(boolean value) {
        this.value = value;
    }
    
    public int convertToInt() {
        if(value) 
            return 1;
        else 
            return 0;
    }
}
