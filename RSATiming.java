import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSATiming {
    
    private static final int ITERATIONS = 30;
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== RSA Timing Tests ===");
        System.out.println();
        
        // Plot 1: Fixed key size, varying plaintext length
        System.out.println("=== Plot 1: Fixed Key Size 1024, Varying Plaintext Length ===");
        System.out.println("KeySize(bits)\tPlaintextLen(bytes)\tEncodeTime(ms)\tDecodeTime(ms)");
        
        int fixedKeySize = 1024;
        int[] plaintextLengths = {16, 32, 48, 64, 80, 96, 112};
        
        for (int len : plaintextLengths) {
            double[] times = timeRSA(fixedKeySize, len);
            System.out.printf("%d\t\t%d\t\t\t%.3f\t\t%.3f%n", 
                    fixedKeySize, len, times[0], times[1]);
        }
        
        System.out.println();
        System.out.println("=== Plot 2: Fixed Plaintext Length 32, Varying Key Size ===");
        System.out.println("KeySize(bits)\tPlaintextLen(bytes)\tEncodeTime(ms)\tDecodeTime(ms)");
        
        int fixedPlainLen = 32;
        int[] keySizes = {512, 1024, 2048};
        
        for (int keySize : keySizes) {
            // Skip key sizes that are too small for the plaintext
            int maxMsgLen = (keySize / 8) - 11;
            if (fixedPlainLen > maxMsgLen) {
                System.out.printf("%d\t\t%d\t\t\t%s\t\t%s%n", 
                        keySize, fixedPlainLen, "N/A", "N/A");
                continue;
            }
            double[] times = timeRSA(keySize, fixedPlainLen);
            System.out.printf("%d\t\t%d\t\t\t%.3f\t\t%.3f%n", 
                    keySize, fixedPlainLen, times[0], times[1]);
        }
    }
    
    public static double[] timeRSA(int keySizeBits, int plaintextLengthBytes) throws Exception {
        // Generate RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySizeBits);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        
        // Generate random plaintext of given length
        byte[] plaintext = new byte[plaintextLengthBytes];
        SecureRandom random = new SecureRandom();
        random.nextBytes(plaintext);
        
        double encodeTimeMs = 0.0;
        double decodeTimeMs = 0.0;
        double timemult = 1.0 / ITERATIONS;
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        
        for (int i = 0; i < ITERATIONS; i++) {
            // Encryption timing (using private key per pseudocode)
            long start = System.nanoTime();
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encrypted = cipher.doFinal(plaintext);
            long end = System.nanoTime();
            encodeTimeMs += (end - start) * timemult / 1_000_000.0;
            
            // Decryption timing (using public key per pseudocode)
            start = System.nanoTime();
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decrypted = cipher.doFinal(encrypted);
            end = System.nanoTime();
            decodeTimeMs += (end - start) * timemult / 1_000_000.0;
        }
        
        return new double[]{encodeTimeMs, decodeTimeMs};
    }
}