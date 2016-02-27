package processor;

import image.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    Bit[][] convertIntToBits(int num) {
        Bit[][] bits = new Bit[8][8];
//        ByteConverter
        return bits;
    }
    
    public void hideInformation(Message message, String key) {
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        Block[][] blocks = image.convertImageToBlocks();
        int bitSize = 0;
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                blocks[i][j].convertToBitplanes();
                listBitplanes.add(blocks[i][j].getBitplanes());
            }
        }
        Bitplane[] messages = message.convertToBitplane();
        Bitplane messagesLengthInfo = new Bitplane();
        ConjugationTable table = new ConjugationTable(message.getLength());
        int n = 0;
        double threshold = 0.3;
        
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();
                if(n < messages.length) {    
                    if(listBitplanes.get(j)[i].isNoisy(threshold)) {
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
                listBitplanes.get(j)[i].deconvertToPBC();
            }
        }
        
        n = 0;
        Bitplane[] tableBitplane = table.getBitplanes();
        System.out.println("Length : " + tableBitplane.length);
        for(int i=0; i<tableBitplane.length; i++) {
            Bit[][] bit = tableBitplane[i].getBits();
            System.out.println(bit.length + " " + bit[i].length);
            for(int j=0; j<bit.length; j++) {
                for(int k=0; k<bit[j].length; k++) {
                    System.out.print(bit[j][k].convertToInt());
                }
                System.out.println();
            }
            System.out.println("*********************");
        }
        
        for(int i = listBitplanes.size()-1; i >= 0; i--) {
            for(int j=listBitplanes.get(i).length; j >= 0; j--) {
                if(n < tableBitplane.length) {    
                    if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                        listBitplanes.get(i)[j] = tableBitplane[n];
                        n++;
                    }
                } else {
                    break;
                }
            }
        }
        
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {    
                listBitplanes.get(j)[i].deconvertToPBC();
            }
        }
        
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                Bitplane[] b = listBitplanes.get(i * blocks[i].length + j);
                blocks[i][j].setBitplanes(b);
                blocks[i][j].deconvertFromBitplanes();
            }
        }
        
         
        
        this.image.convertBlocksToPixels(blocks);
        this.image.convertPixelsToBufferedImage();
        
        // Belom disimpen informasinya ke conjugation table
        // Belom disimpen bitplane informasi ke akhir image.
        // Belom di deconvert jadi pbc lagi
        
        
    }
    
    public void extractInformation(Message message, String key) {
        
    }
    
    public static void main (String[] args) {
        Image image = new Image("flower.png");
        Steganography stegano = new Steganography(image);
        Message message;
        try {
            message = new Message("pesan3.pdf");
            stegano.hideInformation(message, "haha");
        } catch (IOException ex) {
            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
