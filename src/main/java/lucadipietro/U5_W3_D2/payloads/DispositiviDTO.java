package lucadipietro.U5_W3_D2.payloads;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record DispositiviDTO(
        @NotEmpty(message = "Il tipo del dispositivo è obbligatorio") String tipo,
        @NotEmpty(message = "Lo stato del dispositivo è obbligatorio") String stato,
        UUID dipendenteId)
{}
