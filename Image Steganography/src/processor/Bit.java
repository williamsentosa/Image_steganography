package processor;

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
    
    public void setValue(int value) {
        if(value == 0) {
            this.value = false;
        } else if(value == 1) {
            this.value = true;
        }
    }
    
    public int convertToInt() {
        if(value) 
            return 1;
        else 
            return 0;
    }
    
    public static void main(String[] args) {
        Bit b = new Bit();
        b.setValue(true);
        System.out.println(b.convertToInt());
        b.setValue(false);
        System.out.println(b.convertToInt());
        Bit b2 = new Bit(true);
        System.out.println(b2.convertToInt());
    }
}
