package lucadipietro.U5_W3_D2.services;

import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.exceptions.UnauthorizedException;
import lucadipietro.U5_W3_D2.payloads.DipendenteLoginDTO;
import lucadipietro.U5_W3_D2.security.JWTTokenConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendentiService dipendentiService;

    @Autowired
    private JWTTokenConfiguration jwtTokenConfiguration;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUserAndGenerateToken(DipendenteLoginDTO body){
        Dipendente dipendente = this.dipendentiService.findByEmail(body.email());
        if(passwordEncoder.matches(body.password(),dipendente.getPassword())){
            return jwtTokenConfiguration.createToken(dipendente);
        } else {
            throw new UnauthorizedException("Credenziali non corrette!");
        }
    }
}
