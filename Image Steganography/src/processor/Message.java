package processor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author William Sentosa
 */
public class Message {
    private byte[] message;
    private int length;
    
    public Message() {
        length = 0;
    }
    
    public Message(String path) throws IOException {
        Path filePath = Paths.get(path);
        message = Files.readAllBytes(filePath);
        length = message.length;
    }
    
    public Message(int length) {
        this.length = length;
        message = new byte[this.length];
    }
    
    public void setMessage(String path) throws IOException {
        Path filePath = Paths.get(path);
        message = Files.readAllBytes(filePath);
        length = message.length;
    }
    
    public void setLength(int length) {
        this.length = length;
        message = new byte[this.length];
    }
    
    public byte[] getMessage() {
        return message;
    }
    
    public int getLength() {
        return length;
    }
    
    public Bitplane[] convertToBitplane() {
        Bit[][] bits = new Bit[length][8];
        Bitplane[] bitplane;
        ByteConverter byteConverter = new ByteConverter();
        
        // Mengubah semua byte ke bit
        for (int i = 0; i < length; i++) {
            bits[i] = byteConverter.convertByteToBits(message[i]);
        }
        // Mengubah kumpulan bit menjadi kumpulan bitplane
        if ((length % 64) == 0) {
            bitplane = new Bitplane[(length / 64) * 8];
        } else {
            bitplane = new Bitplane[((length / 64) + 1) * 8];
        }
        for (int i = 0; i < ((length / 64) * 8); i++) {
            bitplane[i] = new Bitplane();
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    bitplane[i].setBitsBasedOnPosition(j, k, bits[(i / 8)*64 + j*8 +k][i % 8]);
                }
            }
        }
        if ((length % 64) != 0) {
            // Ada bitplane yang memiliki elemen dummy
            for (int i = ((length / 64) * 8); i < ((length / 64)*8 + 8); i++) {
                bitplane[i] = new Bitplane();
                for (int j = 0; j < ((length % 64) / 8); j++) {
                    for (int k = 0; k < 8; k++) {
                        bitplane[i].setBitsBasedOnPosition(j, k, bits[(i / 8)*64 + j*8 +k][i % 8]);
                    }
                }
                for (int j = 0; j < ((length % 64) % 8); j++) {
                    bitplane[i].setBitsBasedOnPosition((length % 64) / 8, j, bits[(i / 8)*64 + ((length % 64) / 8)*8 +j][i % 8]);
                }
                // Mengisi elemen dummy
                for (int j = ((length % 64) % 8); j < 8; j++) {
                    bitplane[i].setBitsBasedOnPosition((length % 64) / 8, j, new Bit());
                }
                for (int j = ((length % 64) / 8 + 1); j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        bitplane[i].setBitsBasedOnPosition(j, k, new Bit());
                    }
                }
            }
        }
        
//        for (int i = 0; i < bitplane.length; i++) {
//            System.out.println("Bitplane " + i);
//            for (int j = 0; j < 8; j++) {
//                for (int k = 0; k < 8; k++) {
//                    System.out.print(bitplane[i].getBitsBasedOnPosition(j, k).convertToInt() + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
        
        return bitplane;
    }
    
    public void deconvertFromBitplane(Bitplane[] bitplanes) {
        Bit[][] bits = new Bit[length][8];
        ByteConverter byteConverter = new ByteConverter();
        
        for (int i = 0; i < ((length / 64) * 8); i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    bits[(i / 8)*64 + j*8 +k][i % 8] = bitplanes[i].getBitsBasedOnPosition(j, k);
                }
            }
        }
        if ((length % 64) != 0) {
            // Ada bitplane yang memiliki elemen dummy
            for (int i = ((length / 64) * 8); i < ((length / 64)*8 + 8); i++) {
                for (int j = 0; j < ((length % 64) / 8); j++) {
                    for (int k = 0; k < 8; k++) {
                        bits[(i / 8)*64 + j*8 +k][i % 8] = bitplanes[i].getBitsBasedOnPosition(j, k);
                    }
                }
                for (int j = 0; j < ((length % 64) % 8); j++) {
                    bits[(i / 8)*64 + ((length % 64) / 8)*8 +j][i % 8] = bitplanes[i].getBitsBasedOnPosition((length % 64) / 8, j);
                }
            }
        }
        
        for (int i = 0; i < length; i++) {
            message[i] = byteConverter.convertBitsToByte(bits[i]);
        }
    }
    
    public void encrypt() {
        
    }
    
    public void decrypt() {
        
    }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Message message = new Message("C:\\Users\\Windows7\\Desktop\\Kripto\\teks.txt");
        Message message2 = new Message(message.getLength());
        message2.deconvertFromBitplane(message.convertToBitplane());
//        File file = new File("C:\\Users\\Windows7\\Desktop\\Kripto\\doc.docx");
//        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
//        MimeType mimeType = MimeUtil.getMostSpecificMimeType(MimeUtil.getMimeTypes(file));
//        MimeUtil.unregisterMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
//        System.out.println(mimeType.toString());
//        FileOutputStream fos = new FileOutputStream("C:\\Users\\Windows7\\Desktop\\Kripto\\newteks.txt");
//        fos.write(message2.getMessage());
//        fos.close();
    }
    
}
