package lucadipietro.U5_W3_D2.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenConfiguration {

    private String secret;

    public JWTTokenConfiguration(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String createToken(Dipendente dipendente){
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione del token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza del token (una settimana)
                .subject(String.valueOf(dipendente.getId())) // ID del dipendente come soggetto del token (ATTENZIONE A NON INSERIRE DATI SENSIBILI QUA DENTRO!!!!!!)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firma del token con la chiave segreta
                .compact();
    }

    public void verifyToken(String token){
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token); // Il metodo .parse(token) mi lancerà varie eccezioni in caso di token scaduto o malformato o manipolato
        } catch (Exception ex){
            throw new UnauthorizedException("Errore col token, preghiamo di riprovare a fare il login!"); // Se la verifica fallisce, lancia un'eccezione di non autorizzato con status 401
        }
    }
}
