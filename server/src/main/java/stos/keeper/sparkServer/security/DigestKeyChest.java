package stos.keeper.sparkServer.security;

import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.SecretKey;

public class DigestKeyChest {
    private final SecretKey jwtKey;
    private static DigestKeyChest instance;


    private DigestKeyChest() {
        jwtKey = MacProvider.generateKey();
    }

    public static synchronized DigestKeyChest getInstance() {
        if (instance == null) {
            instance = new DigestKeyChest();
        }
        return instance;
    }

    public SecretKey getJwtKey() {
        return jwtKey;
    }
}
