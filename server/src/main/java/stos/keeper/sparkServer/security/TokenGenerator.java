package stos.keeper.sparkServer.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import stos.keeper.model.player.Player;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TokenGenerator {
    public static final int TOKEN_VALIDITY_TIME_HRS = 2;
    private DigestKeyChest keyChest;
    private String username;
    private String password;
    private String email;
    private int hrsValid;


    TokenGenerator(String username, String password, String email, DigestKeyChest keyChest, int hrsValid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.keyChest = keyChest;
        this.hrsValid = hrsValid;
    }

    public TokenGenerator(String username, String password, String email, DigestKeyChest keyChest) {
        this(username, password, email, keyChest, TOKEN_VALIDITY_TIME_HRS);
    }

    public String generateToken() {
        SecretKey jwtKey = keyChest.getJwtKey();
        return Jwts.builder().setExpiration(Date.from(Instant.now().plus(hrsValid, ChronoUnit.HOURS)))
                .setSubject(username)
                .claim("password", password).claim("email", email)
                .signWith(SignatureAlgorithm.HS512, jwtKey).compact();
    }

    public boolean verifyTokenAgainstPlayer(Player player, String token) throws Exception {
        SecretKey jwtKey = keyChest.getJwtKey();
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        boolean verified = claims.getSubject().equals(player.getUserName());
        verified &= claims.getExpiration().toInstant().isAfter(Instant.now());
        verified &= claims.get("password").equals(player.getPassword());
        verified &= claims.get("email").equals(player.getEmail());
        return verified;
    }
}
