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
    
    // Konstruktor
    public VigenereCipher() {
        // do nothing
    }
    
    // Method
    // Mengembalikan Vigenere Cipher Extended, menerapkan Bujursangkar Vigenere
    public String encryptExtended(String plain, String key) {
        int keyIndex;
        String encryption = "";
        
        // Melakukan enkripsi pada setiap karakter di plain text
        for (int i = 0; i < plain.length(); i++) {
            keyIndex = i % key.length();
            encryption += getExtendedCipher(plain.charAt(i), key.charAt(keyIndex));
        }
        
        return encryption;
    }
    
    // Mengembalikan plain text, menerapkan Bujursangkar Vigenere
    public String decryptExtended(String cipher, String key) {
        int keyIndex;
        String decryption = "";
        
        // Melakukan dekripsi pada setiap karakter di cipher text
        for (int i = 0; i < cipher.length(); i++) {
            keyIndex = i % key.length();
            decryption += getExtendedPlain(cipher.charAt(i), key.charAt(keyIndex));
        }
        
        return decryption;
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
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
