package processor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    
    public void printMessage() {
        for(int i=0; i<length; i++) {
            System.out.print(message[i] + " ");
        }
        System.out.println();
    }
    
    public Message(String path) throws IOException {
        byte[] tempMessage;
        int extensionIndex;
        String tempString;
        
        Path filePath = Paths.get(path);
//        tempMessage = Files.readAllBytes(filePath);
//        // Menyisipkan extension file ke dalam message
//        tempString = new String(tempMessage, StandardCharsets.ISO_8859_1);
//        extensionIndex = filePath.toString().lastIndexOf(".");
//        if (extensionIndex == -1) {
//            tempString += ".";
//        } else {
//            tempString += filePath.toString().substring(extensionIndex);
//        }
//        message = tempString.getBytes(StandardCharsets.ISO_8859_1);
        message = Files.readAllBytes(filePath);
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
        int extensionIndex;
        String tempString;
        
        Path filePath = Paths.get(path);
//        tempMessage = Files.readAllBytes(filePath);
//        // Menyisipkan extension file ke dalam message
//        tempString = new String(tempMessage, StandardCharsets.ISO_8859_1);
//        extensionIndex = filePath.toString().lastIndexOf(".");
//        if (extensionIndex == -1) {
//            tempString += ".";
//        } else {
//            tempString += filePath.toString().substring(extensionIndex);
//        }
//        message = tempString.getBytes(StandardCharsets.ISO_8859_1);
        message = Files.readAllBytes(filePath);
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
        if ((length % 63) == 0) {
            bitplane = new Bitplane[(length / 63) * 8];
        } else {
            bitplane = new Bitplane[((length / 63) + 1) * 8];
        }
        for (int i = 0; i < ((length / 63) * 8); i++) {
            bitplane[i] = new Bitplane();
            // Mengisi penanda konjugasi
            bitplane[i].setBitsBasedOnPosition(0, 1, new Bit(true));
            // Mengisi message
            bitplane[i].setBitsBasedOnPosition(0, 0, bits[(i / 8)*63][i % 8]);
            for (int j = 2; j < 8; j++) {
                bitplane[i].setBitsBasedOnPosition(0, j, bits[(i / 8)*63 + j - 1][i % 8]);
            }
            for (int j = 1; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    bitplane[i].setBitsBasedOnPosition(j, k, bits[(i / 8)*63 + j*8 - 1 + k][i % 8]);
                }
            }
        }
        if ((length % 63) != 0) {
            // Ada bitplane yang memiliki elemen dummy
            for (int i = ((length / 63) * 8); i < ((length / 63)*8 + 8); i++) {
                bitplane[i] = new Bitplane();
                // Mengisi penanda konjugasi
                bitplane[i].setBitsBasedOnPosition(0, 1, new Bit(true));
                // Mengisi message
                bitplane[i].setBitsBasedOnPosition(0, 0, bits[(i / 8)*63][i % 8]);
                for (int j = 2; j < 8; j++) {
                    bitplane[i].setBitsBasedOnPosition(0, j, bits[(i / 8)*63 + j - 1][i % 8]);
                }
                for (int j = 1; j < ((length % 63 - 7) / 8 + 1); j++) {
                    for (int k = 0; k < 8; k++) {
                        bitplane[i].setBitsBasedOnPosition(j, k, bits[(i / 8)*63 + j*8 - 1 + k][i % 8]);
                    }
                }
                for (int j = 0; j < ((length % 63 - 7) % 8); j++) {
                    bitplane[i].setBitsBasedOnPosition((length % 63 - 7) / 8 + 1, j, bits[(i / 8)*63 + ((length % 63) / 8)*8 - 1 + j][i % 8]);
                }
                // Mengisi elemen dummy
                for (int j = ((length % 63 - 7) % 8); j < 8; j++) {
                    bitplane[i].setBitsBasedOnPosition((length % 63 - 7) / 8 + 1, j, new Bit());
                }
                for (int j = ((length % 63 - 7) / 8 + 2); j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        bitplane[i].setBitsBasedOnPosition(j, k, new Bit());
                    }
                }
            }
        }
        
        return bitplane;
    }
    
    public void deconvertFromBitplane(Bitplane[] bitplanes) {
        Bit[][] bits = new Bit[length][8];
        ByteConverter byteConverter = new ByteConverter();
        
        for (int i = 0; i < ((length / 63) * 8); i++) {
            bits[(i / 8)*63][i % 8] = bitplanes[i].getBitsBasedOnPosition(0, 0);
            for (int j = 2; j < 8; j++) {
                bits[(i / 8)*63 + j - 1][i % 8] = bitplanes[i].getBitsBasedOnPosition(0, j);
            }
            for (int j = 1; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    bits[(i / 8)*63 + j*8 - 1 + k][i % 8] = bitplanes[i].getBitsBasedOnPosition(j, k);
                }
            }
        }
        if ((length % 63) != 0) {
            // Ada bitplane yang memiliki elemen dummy
            for (int i = ((length / 63) * 8); i < ((length / 63)*8 + 8); i++) {
                bits[(i / 8)*63][i % 8] = bitplanes[i].getBitsBasedOnPosition(0, 0);
                for (int j = 2; j < 8; j++) {
                    bits[(i / 8)*63 + j - 1][i % 8] = bitplanes[i].getBitsBasedOnPosition(0, j);
                }
                for (int j = 1; j < ((length % 63 - 7) / 8 + 1); j++) {
                    for (int k = 0; k < 8; k++) {
                        bits[(i / 8)*63 + j*8 - 1 + k][i % 8] = bitplanes[i].getBitsBasedOnPosition(j, k);
                    }
                }
                for (int j = 0; j < ((length % 63 - 7) % 8); j++) {
                    bits[(i / 8)*63 + ((length % 63) / 8)*8 - 1 + j][i % 8] = bitplanes[i].getBitsBasedOnPosition((length % 63 - 7) / 8 + 1, j);
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
    
    public void save(String path, String fileName) throws FileNotFoundException, IOException {
        byte[] tempMessage;
        FileOutputStream fos;
        int extensionIndex;
        String tempString;
        
//        tempString = new String(message, StandardCharsets.ISO_8859_1);
//        extensionIndex = tempString.lastIndexOf(".");
//        System.out.println(path + fileName + tempString.substring(extensionIndex));
//        tempMessage = tempString.substring(0, extensionIndex).getBytes(StandardCharsets.ISO_8859_1);
//        
//        fos = new FileOutputStream(path + fileName + tempString.substring(extensionIndex));
//        fos.write(tempMessage);
        fos = new FileOutputStream(path + fileName);
        fos.write(message);
        fos.close();
    }
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Message message = new Message("D:\\Semester 6\\Tugas\\Kriptografi\\Tugas Besar 1\\Image_steganography\\Image Steganography\\pesan.docx");
        Message message2;
        
        message.encrypt("tes");
        message2 = new Message(message.getLength());
        message2.deconvertFromBitplane(message.convertToBitplane());
        message2.decrypt("tes");
        message2.save("D:\\Semester 6\\Tugas\\Kriptografi\\Tugas Besar 1\\Image_steganography\\Image Steganography\\", "message.docx");
    }
    
}
