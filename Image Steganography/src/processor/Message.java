package processor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
    private VigenereCipher vigenereCipher;
    
    // Konstruktor
    public Message() {
        length = 0;
        vigenereCipher = new VigenereCipher();
    }
    
    public Message(String path) throws IOException {
        byte[] tempMessage;
        String tempString;
        
        Path filePath = Paths.get(path);
        tempMessage = Files.readAllBytes(filePath);
        tempString = new String(tempMessage, StandardCharsets.ISO_8859_1);
        if (filePath.toString().lastIndexOf(".") == -1) {
            tempString += ".";
        } else {
            tempString += filePath.toString().substring(filePath.toString().lastIndexOf("."));
        }
        message = tempString.getBytes(StandardCharsets.ISO_8859_1);
        length = message.length;
        vigenereCipher = new VigenereCipher();
    }
    
    public Message(int length) {
        this.length = length;
        message = new byte[this.length];
        vigenereCipher = new VigenereCipher();
    }
    
    // Setter
    public void setMessage(String path) throws IOException {
        byte[] tempMessage;
        String tempString;
        
        Path filePath = Paths.get(path);
        tempMessage = Files.readAllBytes(filePath);
        tempString = new String(tempMessage, StandardCharsets.ISO_8859_1);
        if (filePath.toString().lastIndexOf(".") == -1) {
            tempString += ".";
        } else {
            tempString += filePath.toString().substring(filePath.toString().lastIndexOf("."));
        }
        message = tempString.getBytes(StandardCharsets.ISO_8859_1);
        length = message.length;
    }
    
    public void setLength(int length) {
        this.length = length;
        message = new byte[this.length];
    }
    
    public void setVigenereCipher(VigenereCipher vigenereCipher) {
        this.vigenereCipher = vigenereCipher;
    }
    
    // Getter
    public byte[] getMessage() {
        return message;
    }
    
    public int getLength() {
        return length;
    }
    
    public VigenereCipher getVigenereCipher() {
        return vigenereCipher;
    }
    
    // Method
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
    
    public void encrypt(String key) {
        String tempMessage = new String(message, StandardCharsets.ISO_8859_1);
        tempMessage = vigenereCipher.encryptExtended(tempMessage, key);
        message = tempMessage.getBytes(StandardCharsets.ISO_8859_1);
    }
    
    public void decrypt(String key) {
        String tempMessage = new String(message, StandardCharsets.ISO_8859_1);
        tempMessage = vigenereCipher.decryptExtended(tempMessage, key);
        message = tempMessage.getBytes(StandardCharsets.ISO_8859_1);
    }
    
    public void save(String fileName, String fileExtension) {
        FileOutputStream fos;
        String path = "C:\\Users\\Windows7\\Desktop\\Kripto\\", tempString;
        
        tempString = new String(message, StandardCharsets.ISO_8859_1);
        
    }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Message message = new Message("C:\\Users\\Windows7\\Desktop\\Kripto\\teks.txt");
        message.encrypt("tes");
        message.decrypt("tes");
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Windows7\\Desktop\\Kripto\\newteks.txt");
        fos.write(message.getMessage());
        fos.close();
    }
    
}
