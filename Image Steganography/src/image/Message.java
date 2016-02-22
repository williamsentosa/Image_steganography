package image;

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
        // konversi message ke bitplane
    }
    
    public void deconvertFromBitplane(Bitplane[] bitplanes) {
        // konversi bitplanes ke message
    }
    
}
