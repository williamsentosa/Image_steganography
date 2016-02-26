package processor;

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
    }
    
    public void setMessage(String path) throws IOException {
        Path filePath = Paths.get(path);
        message = Files.readAllBytes(filePath);
    }
    
    public byte[] getMessage() {
        return message;
    }
    
    public Bitplane[] convertToBitplane() {
        Bit[][] bits = new Bit[message.length][8];
        Bitplane[] bitplane;
        ByteConverter byteConverter = new ByteConverter();
        
        // Mengubah semua byte ke bit
        for (int i = 0; i < message.length; i++) {
            bits[i] = byteConverter.convertByteToBits(message[i]);
        }
        // Mengubah kumpulan bit menjadi kumpulan bitplane
        if ((message.length % 64) == 0) {
            bitplane = new Bitplane[(message.length / 64) * 8];
        } else {
            bitplane = new Bitplane[((message.length / 64) + 1) * 8];
        }
        for (int i = 0; i < ((message.length / 64) * 8); i++) {
            bitplane[i] = new Bitplane();
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    bitplane[i].setBitsBasedOnPosition(j, k, bits[(i / 8)*64 + j*8 +k][i % 8]);
                }
            }
        }
        if ((message.length % 64) != 0) {
            // Ada bitplane yang memiliki elemen dummy
            for (int i = ((message.length / 64) * 8); i < ((message.length / 64)*8 + 8); i++) {
                bitplane[i] = new Bitplane();
                for (int j = 0; j < ((message.length % 64) / 8); j++) {
                    for (int k = 0; k < 8; k++) {
                        bitplane[i].setBitsBasedOnPosition(j, k, bits[(i / 8)*64 + j*8 +k][i % 8]);
                    }
                }
                for (int j = 0; j < ((message.length % 64) % 8); j++) {
                    bitplane[i].setBitsBasedOnPosition((message.length % 64) / 8, j, bits[(i / 8)*64 + ((message.length % 64) / 8)*8 +j][i % 8]);
                }
                // Mengisi elemen dummy
                for (int j = ((message.length % 64) % 8); j < 8; j++) {
                    bitplane[i].setBitsBasedOnPosition((message.length % 64) / 8, j, new Bit());
                }
                for (int j = ((message.length % 64) / 8 + 1); j < 8; j++) {
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
        Bit[] bits;
        ByteConverter byteConverter = new ByteConverter();
        
//        for (int i = 0; i < bitplanes.length; i++) {
//            for (int j = 0; j < bitplanes)
//        }
//        
//        message = byteConverter.convertBitsToByte(bits);
    }
    
    public void encrypt() {
        
    }
    
    public void decrypt() {
        
    }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Message message = new Message("C:\\Users\\Windows7\\Desktop\\Kripto\\teks.txt");
        message.convertToBitplane();
//        File file = new File("C:\\Users\\Windows7\\Desktop\\Kripto\\doc.docx");
//        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
//        MimeType mimeType = MimeUtil.getMostSpecificMimeType(MimeUtil.getMimeTypes(file));
//        MimeUtil.unregisterMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
//        System.out.println(mimeType.toString());
//        FileOutputStream fos = new FileOutputStream("C:\\Users\\Windows7\\Desktop\\Kripto\\tes2");
//        fos.write(message.getMessage());
//        fos.close();
    }
    
}
