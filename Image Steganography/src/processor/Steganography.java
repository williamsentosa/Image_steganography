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
        double threshold = 0.5;
        
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
        System.out.println("Length : " + message.getLength());
        msgLengthInfo.setBits(converter.convertIntegerToBitplane(message.getLength()));
        boolean found = false;
        for(int i = listBitplanes.size()-1; i >= 0; i--) {
            for(int j=listBitplanes.get(i).length - 1; j >= 0; j--) {        
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                    if(!msgLengthInfo.isNoisy(threshold)) {
                        msgLengthInfo.conjugate();
                    }
                    System.out.println("i : " + i + " j : " + j);
                    listBitplanes.get(i)[j] = msgLengthInfo;
                    System.out.println("**** Mesage Info ****");
                    for(int a = 0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                        for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                            System.out.print(msgLengthInfo.getBits()[a][b].convertToInt());
                        }
                        System.out.println();
                    }
                    System.out.println("**** Bitplane info ****");
                    for(int a = 0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                        for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                            System.out.print(listBitplanes.get(i)[j].getBits()[a][b].convertToInt());
                        }
                        System.out.println();
                    }
                    found = true;
                    break;
                }
            }
            if(found) break;
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
        ArrayList<Bitplane[]> listBitplanes = new ArrayList<>();
        Block[][] blocks = image.convertImageToBlocks();
        for(int i=0; i<blocks.length; i++) {
            for(int j=0; j<blocks[i].length; j++) {
                blocks[i][j].convertToBitplanes();
                listBitplanes.add(blocks[i][j].getBitplanes());
            }
        }
        boolean found = false;
        double threshold = 0.5;
        int messagesLength = 0;
        ByteConverter converter = new ByteConverter();
        for(int i = listBitplanes.size()-1; i >= 0; i--) {
            for(int j=listBitplanes.get(i).length - 1; j >= 0; j--) {        
                if(listBitplanes.get(i)[j].isNoisy(threshold)) {
                    System.out.println("i : " + i + " j : " + j);
                    if(listBitplanes.get(i)[j].isConjugated()) {
                        System.out.println("**** Before ***");
                        for(int a = 0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                            for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                                System.out.print(listBitplanes.get(i)[j].getBits()[a][b].convertToInt());
                            }
                            System.out.println();
                        }
                        listBitplanes.get(i)[j].conjugate();
                        System.out.println("**** After ***");
                        for(int a = 0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                            for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                                System.out.print(listBitplanes.get(i)[j].getBits()[a][b].convertToInt());
                            }
                            System.out.println();
                        }
                        messagesLength = converter.convertBitplaneToInteger(listBitplanes.get(i)[j].getBits());
                        System.out.println("Conjugated");
                    } else {
                        for(int a = 0; a<listBitplanes.get(i)[j].getBits().length; a++) {
                            for(int b=0; b<listBitplanes.get(i)[j].getBits()[a].length; b++) {
                                System.out.print(listBitplanes.get(i)[j].getBits()[a][b].convertToInt());
                            }
                            System.out.println();
                        }
                        messagesLength = converter.convertBitplaneToInteger(listBitplanes.get(i)[j].getBits());
                        System.out.println(" not Conjugated");
                    }
                    found = true;
                    break;
                }
            }
            if(found) break;
        }
        System.out.println(messagesLength);
        Message message = new Message(messagesLength);
        int n = 0;
        Bitplane[] messages = new Bitplane[(messagesLength)/8 + 1];
        for(int j=0; j<listBitplanes.size(); j++) {
            for(int i=0; i<listBitplanes.get(j).length; i++) {
                listBitplanes.get(j)[i].convertToCGC();    
                if(listBitplanes.get(j)[i].isNoisy(threshold)) {
                    if(listBitplanes.get(j)[i].isConjugated()) {
                        messages[n] = listBitplanes.get(j)[i];
                        messages[n].conjugate();
                    } else {
                        messages[n] = listBitplanes.get(j)[i];
                    }
                    n++;
                }
            }
        }
        message.deconvertFromBitplane(messages);
        return message;
    }
    
    public static void main (String[] args) {
        Image image = new Image("flower.png");
        Steganography stegano = new Steganography(image);
        Message message;
        try {
            message = new Message("pesan.docx");
            System.out.println(message.getLength());
            stegano.hideInformation(message, "haha");
        } catch (IOException ex) {
            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        image = new Image("new1.png");
        stegano = new Steganography(image);
        message = stegano.extractInformation(null);
        try {
            message.save("D:\\Semester 6\\Tugas\\Kriptografi\\Tugas Besar 1\\Image_steganography\\Image Steganography", "message");
        } catch (IOException ex) {
            Logger.getLogger(Steganography.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
