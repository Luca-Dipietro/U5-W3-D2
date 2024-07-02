package lucadipietro.U5_W3_D2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.exceptions.UnauthorizedException;
import lucadipietro.U5_W3_D2.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTokenConfiguration jwtTokenConfiguration;
    @Autowired
    private DipendentiService dipendentiService;

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

        // Se vogliamo abilitare l'autorizzazione dobbiamo 'informare' Spring Security di chi sia l'utente che sta effettuando la richiesta
        String dipendenteID = jwtTokenConfiguration.extractIdFromToken(accessToken);
        Dipendente current = dipendentiService.findById(UUID.fromString(dipendenteID));

        // Associo l'utente trovato al security context
        Authentication authentication = new UsernamePasswordAuthenticationToken(current,null,current.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }

    // Metodo che determina se il filtro deve essere applicato a una particolare richiesta.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{

        // Non applica il filtro alle richieste che corrispondono al percorso "/auth/**".
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
