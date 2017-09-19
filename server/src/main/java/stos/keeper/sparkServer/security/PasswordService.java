package stos.keeper.sparkServer.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordService {

    private static final String SHA_1_PRNG = "SHA1PRNG";
    private static final String ENCRYPTION_ALGO = "PBKDF2WithHmacSHA1";

    public static String generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(SHA_1_PRNG);
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String encryptPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int derivedKeyLength = 160;
        int iterations = 20000;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), iterations, derivedKeyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGO);
        byte[] encoded = keyFactory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }

    public static boolean authenticate(String attemptedPassword, String encryptedPassword, String generatedSalt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String encryptedAttemptedPassword = encryptPassword(attemptedPassword, generatedSalt);
        return encryptedAttemptedPassword.equals(encryptedPassword);
    }
}
