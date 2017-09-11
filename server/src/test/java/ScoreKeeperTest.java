import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.Test;

import javax.crypto.SecretKey;

public class ScoreKeeperTest {
    @Test
    public void testtest() throws Exception {
        SecretKey key = MacProvider.generateKey();
        String jws = Jwts.builder().setSubject("gah").setIssuer("scorekeeper").signWith(SignatureAlgorithm.HS256, key)
                .compact();

        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody().getSubject();
        System.out.println(key.getAlgorithm());
        System.out.println(new String(key.getEncoded()));
        System.out.println(jws);
        System.out.println(subject);
    }
}
