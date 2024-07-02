package lucadipietro.U5_W3_D2.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lucadipietro.U5_W3_D2.enums.RuoloDipendente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"password", "role", "authorities", "enabled", "accountNonExpired", "credentialsNonExpired", "accountNonLocked"})
public class Dipendente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String email;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private RuoloDipendente ruolo;

    public Dipendente(String username, String password, String nome, String cognome, String email) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo = RuoloDipendente.DIPENDENTE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
