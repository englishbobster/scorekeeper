package stos.keeper.sparkServer.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordService {

    private static final String SHA_1_PRNG = "SHA1PRNG";
    private static final String ENCRYPTION_ALGO = "PBKDF2WithHmacSHA1";

    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance(SHA_1_PRNG);
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] encryptPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int derivedKeyLength = 160;
        int iterations = 20000;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGO);
        return keyFactory.generateSecret(spec).getEncoded();
    }
}
