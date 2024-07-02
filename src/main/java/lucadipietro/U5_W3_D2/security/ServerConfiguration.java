package lucadipietro.U5_W3_D2.security;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class ServerConfiguration {
    @Bean
    public Cloudinary uploader(
            @Value("${cloudinary.name}") String name,
            @Value("${cloudinary.secret}") String secret,
            @Value("${cloudinary.key}") String key)
    {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("cloud_name", name);
        configuration.put("api_key", key);
        configuration.put("api_secret", secret);
        return new Cloudinary(configuration);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(http -> http.disable()); // Disabilita il supporto per il form login.
        httpSecurity.csrf(http -> http.disable());  // Disabilita la protezione CSRF (Complicherebbe il tutto per le nostre app)
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Non voglio le sessioni (perché con JWT non si utilizzano le sessioni)
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll()); // Evita di avere 401 per ogni richiesta
        return httpSecurity.build();
    }
}
