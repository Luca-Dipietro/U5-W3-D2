package lucadipietro.U5_W3_D2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lucadipietro.U5_W3_D2.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTokenConfiguration jwtTokenConfiguration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera l'header "Authorization" dalla richiesta.
        String authHeader = request.getHeader("Authorization");

        // Verifica se l'header è presente e se inizia con "Bearer ".
        if(authHeader == null || !authHeader.startsWith("Bearer ")) throw new UnauthorizedException("Il token inserito è sbagliato, per favore inserisci quello corretto!");

        // Estrae il token JWT dall'header (senza il prefisso "Bearer ").
        String accessToken= authHeader.substring(7);

        // Verifica la validità del token JWT.
        jwtTokenConfiguration.verifyToken(accessToken);

        filterChain.doFilter(request,response);
    }

    // Metodo che determina se il filtro deve essere applicato a una particolare richiesta.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{

        // Non applica il filtro alle richieste che corrispondono al percorso "/auth/**".
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
