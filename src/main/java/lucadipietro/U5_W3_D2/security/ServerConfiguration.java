package lucadipietro.U5_W3_D2.security;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
        httpSecurity.cors(Customizer.withDefaults()); // NON DIMENTICARE QUESTA IMPOSTAZIONE SE SI UTILIZZA UNA CONFIGURAZIONE CORS PERSONALIZZATA
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll()); // Evita di avere 401 per ogni richiesta
        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // whitelist dei frontend che possono accedere a questo backend
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Registro la configurazione CORS appena fatta a livello globale su tutti gli endpoint del mio server
        return source;
    }

    @Bean
    PasswordEncoder getBCrypt(){
        return new BCryptPasswordEncoder(11);
        // 11 significa che l'algoritmo verrà eseguito 2^11 volte, cioè 2048. Su un computer di prestazioni medie ciò significa all'incirca 100/200ms
    }
}
