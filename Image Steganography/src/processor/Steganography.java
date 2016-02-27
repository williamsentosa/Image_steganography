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
    
    /**
     * 
     * @param message true if message can be hidden and false if it can't
     * @param key
     * @return 
     */
    public boolean hideInformation(Message message, String key) {
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        Block[][] blocks = image.convertImageToBlocks();
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                blocks[i][j].convertToBitplanes();
                listBitplanes.add(blocks[i][j].getBitplanes());
            }
        }
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();
            }
        }
        
        Bitplane[] messages = message.convertToBitplane();
        System.out.println("Message length : " + message.getLength());
        System.out.println("Bitplane length = " + messages.length);
        int n = -1;
        double threshold = 0.3;
        Bitplane msgLengthInfo = new Bitplane();
        System.out.println("Length : " + message.getLength());
        ByteConverter converter = new ByteConverter();
        msgLengthInfo.setBits(converter.convertIntegerToBitplane(message.getLength()));
        for(int i=0; i<listBitplanes.size(); i++) {
            for(int j=0; j<listBitplanes.get(i).length; j++) {
                if( n == -1 ) {
                    if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                        if(!msgLengthInfo.isNoisy(threshold)) {
                            msgLengthInfo.conjugate();
                        }
                        listBitplanes.get(i)[j] = msgLengthInfo;
                        System.out.println("i : " + i + " j : " + j);
                        for(int a=0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                            for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                                System.out.print(listBitplanes.get(i)[j].getBits()[a][b].convertToInt());
                            }
                            System.out.println();
                        }
                        n++;
                    }
                } else if(n < messages.length) {    
                    if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                        if(messages[n].isNoisy(threshold)) {
                            listBitplanes.get(i)[j] = messages[n];
                        } else {
                            messages[n].conjugate();
                            listBitplanes.get(i)[j] = messages[n];
                        }
                        System.out.println("Taruh i : " + i + " j : " + j);
                        n++;
                    }
                }
            }
        }
        
        if(n < messages.length) return false;
  
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {    
                listBitplanes.get(j)[i].deconvertToPBC();
            }
        }
        n=0;
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                Bitplane[] b = listBitplanes.get(n);
                blocks[i][j].setBitplanes(b);
                blocks[i][j].deconvertFromBitplanes();
                n++;
            }
        }
                
        this.image.convertBlocksToPixels(blocks);
        this.image.convertPixelsToBufferedImage();      
        return true;
    }
   
    public Message extractInformation(String key) {
        image = new Image("new1.png");
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        Block[][] blocks = image.convertImageToBlocks();
        Message message = null;
        int x,y;
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                blocks[i][j].convertToBitplanes();
                listBitplanes.add(blocks[i][j].getBitplanes());
            }
        }
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();
            }
        }
  
        boolean found = false;
        double threshold = 0.3;
        int messagesLength = 0;
        ByteConverter converter = new ByteConverter();
        for(int i=0; i<listBitplanes.size(); i++) {
            for(int j=0; j<listBitplanes.get(i).length; j++) {
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                    System.out.println("i : " + i + " j : " + j);
                    for(int a=0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                        for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                            System.out.print(listBitplanes.get(i)[j].getBits()[a][b].convertToInt());
                        }
                        System.out.println();
                    }
                    if(listBitplanes.get(i)[j].isConjugated()) {
                        listBitplanes.get(i)[j].conjugate();
                        messagesLength = converter.convertBitplaneToInteger(listBitplanes.get(i)[j].getBits());
                        listBitplanes.get(i)[j].conjugate();
                    } else {
                        messagesLength = converter.convertBitplaneToInteger(listBitplanes.get(i)[j].getBits());
                    }
                    found = true;
                    break;
                }
            }
            if(found) break;
        }
        
        System.out.println("Message Length : " + messagesLength);
        message = new Message(messagesLength);
        
        int n = -1;
        Bitplane[] messages;
        if((messagesLength % 63) == 0) {
            messages = new Bitplane[(messagesLength/63)*8];
        } else {
            messages = new Bitplane[((messagesLength/63)+1)*8];
        }
        for(int i=0; i<listBitplanes.size(); i++) {
            for(int j=0; j<listBitplanes.get(i).length; j++) {
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                    if(n == -1) {
                        n++;
                    } else if(n < messages.length) {
                        System.out.println("Hasil i : " + i + " j : " + j);
                        if(listBitplanes.get(i)[j].isConjugated()) {
                            messages[n] = listBitplanes.get(i)[j];
                            messages[n].conjugate();
                        } else {
                            messages[n] = listBitplanes.get(i)[j];
                        }
                        n++;
                    } else {
                        break;
                    }
                }
            }
        }
        System.out.println("n : " + n);
        System.out.println("Bitplane length = " + messages.length);
        message.deconvertFromBitplane(messages);
        return message;
    }
    
    public static void main (String[] args) {
        Image image = new Image("flower.png");
        Steganography stegano = new Steganography(image);
        Message message;
        Boolean success = true;
        try {
            message = new Message("pesan.docx");
            message.printMessage();
            success = stegano.hideInformation(message, "haha");
            if(!success) System.out.println("File is too big to be hidden");
        } catch (IOException ex) {
            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(success) {
            Image image2 = new Image("new1.png");
            Steganography stegano2 = new Steganography(image2);
            Message message2 = stegano2.extractInformation(null);
            message2.printMessage();
            try {
                message2.save("D:\\Semester 6\\Tugas\\Kriptografi\\Tugas Besar 1\\Image_steganography\\Image Steganography\\", "hasil");
            } catch (IOException ex) {
                Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
