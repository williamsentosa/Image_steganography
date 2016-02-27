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
        System.out.println("Message length : " + message.getLength());
        int n = 0;
        double threshold = 0.3;
        
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();
                if(n < messages.length) {    
                    if(listBitplanes.get(j)[i].isNoisy(threshold)) {
                        if(messages[n].isNoisy(threshold)) {
                            listBitplanes.get(j)[i] = messages[n];
                        } else {
                            messages[n].conjugate();
                            listBitplanes.get(j)[i] = messages[n];
                        }
                        n++;
                    }
                }
            }
        }
        
        ByteConverter converter = new ByteConverter();
        Bitplane msgLengthInfo = new Bitplane();
        msgLengthInfo.setBits(converter.convertIntegerToBitplane(message.getLength()));
        
        for(int i = listBitplanes.size()-1; i >= 0; i--) {
            for(int j=listBitplanes.get(i).length - 1; j >= 0; j--) {        
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                    listBitplanes.get(i)[j] = msgLengthInfo;
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
    }
    
    public Message extractInformation(String key) {
        Message message = new Message();
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        Block[][] blocks = image.convertImageToBlocks();
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                blocks[i][j].convertToBitplanes();
                listBitplanes.add(blocks[i][j].getBitplanes());
            }
        }
        double threshold = 0.3;
        int messagesLength = 0;
        ByteConverter converter = new ByteConverter();
        for(int i = listBitplanes.size()-1; i >= 0; i--) {
            for(int j=listBitplanes.get(i).length - 1; j >= 0; j--) {        
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                    if(listBitplanes.get(i)[j].isConjugated()) {
                        listBitplanes.get(i)[j].conjugate();
                        messagesLength = converter.convertBitplaneToInteger(listBitplanes.get(i)[j].getBits());
                    } else {
                        messagesLength = converter.convertBitplaneToInteger(listBitplanes.get(i)[j].getBits());
                    }
                    break;
                }
            }
        }
        int n = 0;
        Bitplane[] messages = new Bitplane[(messagesLength)/8 + 1];
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();    
                if(listBitplanes.get(j)[i].isNoisy(threshold)) {
                    if(listBitplanes.get(j)[i].isConjugated()) {
                        listBitplanes.get(j)[i] = messages[n];
                        messages[n].conjugate();
                    } else {
                        messages[n] = listBitplanes.get(j)[i];
                    }
                    n++;
                }
            }
        }
        message.setLength(messagesLength);
        message.deconvertFromBitplane(messages);
        return message;
    }
    
    public static void main (String[] args) {
//        Image image = new Image("flower.png");
//        Steganography stegano = new Steganography(image);
//        Message message;
//        try {
//            message = new Message("pesan3.pdf");
//            stegano.hideInformation(message, "haha");
//        } catch (IOException ex) {
//            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        Image image = new Image("new1.png");
        Steganography stegano = new Steganography(image);
        Message message;
        message = stegano.extractInformation(null);
        
        
    }
    
}
