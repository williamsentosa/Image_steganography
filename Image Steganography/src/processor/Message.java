package processor;

/**
 *
 * @author William Sentosa
 */
public class Message {
    private byte[] message;
    
    public Message() {
        // do nothing
    }
    
    public Message(String path) {
        // convert path ke message
    }
    
    public void setMessage(String path) {
        // convert path ke message
    }
    
    public byte[] getMessage() {
        return message;
    }
    
    public Bitplane[] convertToBitplane() {
        Bitplane[] result = new Bitplane[0];
        return result;
    }
    
    public void deconvertFromBitplane(Bitplane[] bitplanes) {
        // konversi bitplanes ke message
    }
    
}
