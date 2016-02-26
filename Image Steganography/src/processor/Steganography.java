package processor;

import image.Image;
import java.util.ArrayList;

/**
 *
 * @author William Sentosa
 */
public class Steganography {
    private Image image;
    
    public Steganography() {
        image = new Image();
    }
    
    public Steganography(Image image) {
        this.image = image;
    }
    
    public Image getImage() {
        return image;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    public void hideInformation(Message message, String key) {
//        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
//        image.getBytesFromImage();
//        Block[][] blocks = image.convertBytesToBlock();       
//        Bitplane[][] bitplanes = new Bitplane[blocks.length][8];
//        for(int i=0; i<blocks.length; i++) {
//            for(int j=0; j<blocks[i].length; j++) {
//                listBitplanes.add(blocks[i][j].convertToBitplanes(matrixOfBytes));
//            }
//        }
//        Bitplane[] messages = message.convertToBitplane();
//        int n = 0;
//        for(int j=0; j<listBitplanes.size(); j++) {
//            for(int i=0; i<listBitplanes.get(j).length; i++) {
//                listBitplanes.get(j)[i].convertToCGC();
//                if(listBitplanes.get(j)[i].isNoisy(0.3) && n < messages.length) {
//                    // messages belom di cek apakah noisy ato blm
//                    // blm dimasukin ke conjugation table
//                    listBitplanes.get(j)[i] = messages[n];
//                    n++;
//                }
//            }
//        }
        // Belom disimpen informasinya ke conjugation table
        // Belom disimpen bitplane informasi ke akhir image.
        // Belom di deconvert jadi pbc lagi
        
        
    }
    
    public void extractInformation(Message message, String key) {
        
    }
    
}
