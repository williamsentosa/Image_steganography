/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processor;

/**
 *
 * @author angelynz95
 */
public class VigenereCipher {
    // Atribut
    private String textResult;
    
    // Konstruktor
    public VigenereCipher() {
        textResult = "";
    }
    
    // Getter
    public String getTextResult() {
        return textResult;
    }
    
    // Method
    // Mengembalikan Vigenere Cipher Extended, menerapkan Bujursangkar Vigenere
    public void encryptExtended(String plain, String key) {
        int keyIndex;
        String encryption = "";
        
        // Melakukan enkripsi pada setiap karakter di plain text
        for (int i = 0; i < plain.length(); i++) {
            keyIndex = i % key.length();
            encryption += getExtendedCipher(plain.charAt(i), key.charAt(keyIndex));
        }
        
        textResult = encryption;
    }
    
    // Mengembalikan plain text, menerapkan Bujursangkar Vigenere
    public void decryptExtended(String cipher, String key) {
        int keyIndex;
        String decryption = "";
        
        // Melakukan dekripsi pada setiap karakter di cipher text
        for (int i = 0; i < cipher.length(); i++) {
            keyIndex = i % key.length();
            decryption += getExtendedPlain(cipher.charAt(i), key.charAt(keyIndex));
        }
        
        textResult = decryption;
    }
    
    // Mengembalikan Vigenere Cipher Extended, menerapkan Bujursangkar Vigenere
    private char getExtendedCipher(char plain, char key) {
        int plainAscii = (int) plain;
        int keyAscii = (int) key;
        
        char cipher = (char) ((plainAscii + keyAscii) % 256);
        
        return cipher;
    }
    
    // Mengembalikan plain char, menerapkan Bujursangkar Vigenere
    private char getExtendedPlain(char cipher, char key) {
        int cipherAscii = (int) cipher;
        int keyAscii = (int) key;
        
        char plain = (char) ((cipherAscii + 256 - keyAscii) % 256);
        
        return plain;
    }
    
    // Mengembalikan textResult tanpa spasi
    public String getTextResultNoSpace() {
        String text = textResult.replaceAll(" ", "");
        text = text.replaceAll("\n", "");
        text = text.replaceAll("\t", "");
        
        return text;
    }
    
    // Mengembalikan textResult dalam kelompok 5-huruf
    public String getTextResultInFive() {
        int count = 0, i = 0;
        String temp = getTextResultNoSpace(), text = "";
        
        while (i < temp.length()) {
            if ((i + 5) < temp.length()) {
                text += temp.substring(i, i + 5);
            } else {
                text += temp.substring(i, temp.length());
            }
            text += " ";
            
            i+=5;
        }
        
        return text;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
