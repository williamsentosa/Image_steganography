package processor;

import image.Image;
import java.util.ArrayList;

/**
 *
 * @author William Sentosa
 */
public class Steganography {
    private Image image;
    private ConjugationTable table;
    
    public Steganography() {
        image = new Image();
        table = new ConjugationTable();
    }
    
    public Steganography(Image image) {
        this.image = image;
        table = new ConjugationTable();
    }
    
    public Image getImage() {
        return image;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    public void hideInformation(Message message, String key) {
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        image.getBytesFromImage();
        Block[][] blocks = image.convertBytesToBlocks();
        int bitSize = 0;
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                listBitplanes.add(blocks[i][j].convertToBitplanes());
            }
        }
        Bitplane[] messages = message.convertToBitplane();
        int n = 0;
        double threshold = 0.3;
        
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();
                if(listBitplanes.get(j)[i].isNoisy(0.3) && n < messages.length) {
                    if(messages[n].isNoisy(threshold)) {
                        table.addSign(new Bit(false));
                        listBitplanes.get(j)[i] = messages[n];
                    } else {
                        messages[n].conjugate();
                        listBitplanes.get(j)[i] = messages[n];
                        table.addSign(new Bit(true));
                    }
                    n++;
                }
            }
        }
        // Belom disimpen informasinya ke conjugation table
        // Belom disimpen bitplane informasi ke akhir image.
        // Belom di deconvert jadi pbc lagi
        
        
    }
    
    public void extractInformation(Message message, String key) {
        
    }
    
}
