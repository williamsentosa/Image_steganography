package processor;

import image.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Sentosa - 13513026
 * @author Candy Olivia Mawalim - 13513031
 * @author Angela Lynn - 13513032
 */
public class Steganography {
    private Image image;
    private int x = 134;
    private int y = 2;
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
    public boolean hideInformation(Message message, double threshold, String filename) {
        System.out.println("*** Hiding ***");
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
        
        
        System.out.println("x : " + x + " y : " + y);
        for(int a=0; a<listBitplanes.get(x)[y].getBits().length; a++) {
            for(int b=0; b<listBitplanes.get(x)[y].getBits()[a].length; b++) {
                System.out.print(listBitplanes.get(x)[y].getBits()[a][b].convertToInt());
            }
            System.out.println();
        }
        
        Bitplane[] messages = message.convertToBitplane();
//        System.out.println("Message length : " + message.getLength());
//        System.out.println("Bitplane length = " + messages.length);
//        System.out.print("Pesan ke " + (messages.length-100) + " : ");
//        message.printMessage(messages.length - 100);
        
        int n = -1;
        Bitplane msgLengthInfo = new Bitplane();
        
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
                        //System.out.println("Value : " + listBitplanes.get(i)[j].checkNoisy());
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
        System.out.println("Bitplane size : " + listBitplanes.size() * 8);
        this.image.convertBlocksToPixels(blocks);
        this.image.convertPixelsToBufferedImage(filename);      
        return true;
    }
     
    public Message extractInformation(double threshold) {
        System.out.println("*** Ekstraksi ***");
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        Block[][] blocks = image.convertImageToBlocks();
        
        Message message = null;
        int x,y;
        x = this.x;
        y = this.y;
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
        System.out.println("Bitplane size : " + listBitplanes.size() * 8);
        boolean found = false;
        int messagesLength = 0;
        ByteConverter converter = new ByteConverter();
        for(int i=0; i<listBitplanes.size(); i++) {
            for(int j=0; j<listBitplanes.get(i).length; j++) {
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
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
                        //System.out.println("Value : " + listBitplanes.get(i)[j].checkNoisy());
                        if(listBitplanes.get(i)[j].isConjugated()) {
                            listBitplanes.get(i)[j].conjugate();
                            messages[n] = listBitplanes.get(i)[j];
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
        System.out.print("Pesan ke " + (n-100) + " : ");
        //message.printMessage(n-100);
        return message;
    }
    
    public static void main (String[] args) {
        Image image = new Image("flower.png");
        Steganography stegano = new Steganography(image);
        Message message;
        double threshold = (double) 0.4;
        Boolean success = true;
        String filename = "hasil.png";
        try {
            message = new Message("pesan2.docx");
            message.printMessage();
            success = stegano.hideInformation(message, threshold, filename);
            if(!success) System.out.println("File is too big to be hidden");
        } catch (IOException ex) {
            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
        }
        double threshold2 = 0.4;
        String filename2 = "hasil.png";
        Image image2 = new Image(filename2);
        Steganography stegano2 = new Steganography(image2);
        Message message2 = stegano2.extractInformation(threshold2);
        message2.printMessage();
        try {
            message2.save("D:\\Semester 6\\Tugas\\Kriptografi\\Tugas Besar 1\\Image_steganography\\Image Steganography\\", "hasil");
        } catch (IOException ex) {
            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
