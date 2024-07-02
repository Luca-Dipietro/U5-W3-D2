package lucadipietro.U5_W3_D2.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.exceptions.BadRequestException;
import lucadipietro.U5_W3_D2.exceptions.NotFoundException;
import lucadipietro.U5_W3_D2.payloads.DipendentiDTO;
import lucadipietro.U5_W3_D2.repositories.DipendentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DipendentiService {
    @Autowired
    private DipendentiRepository dipendentiRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Dipendente> getDipendenti(int pageNumber, int pageSize, String sortBy){
        if(pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        return dipendentiRepository.findAll(pageable);
    }

    public Dipendente save(DipendentiDTO body){
        this.dipendentiRepository.findByUsername(body.username()).ifPresent(
                dipendente -> {
                    throw new BadRequestException("Esiste già un dipendente con questo username " + body.username());
                }
        );
        this.dipendentiRepository.findByEmail(body.email()).ifPresent(
                dipendente -> {
                    throw new BadRequestException("Esiste già un dipendente con questa email " + body.email());
                }
        );
        Dipendente newDipendente = new Dipendente(body.username(), passwordEncoder.encode(body.password()), body.nome(), body.cognome(), body.email());
        newDipendente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return this.dipendentiRepository.save(newDipendente);
    }

    public Dipendente findById(UUID dipendenteId) {
        return this.dipendentiRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }

    public Dipendente findByIdAndUpdate(UUID dipendenteId, DipendentiDTO body){
        Dipendente found = this.findById(dipendenteId);
        found.setUsername(body.username());
        found.setPassword(passwordEncoder.encode(body.password()));
        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setEmail(body.email());
        found.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return this.dipendentiRepository.save(found);
    }

    public void findByIdAndDelete(UUID dipendenteId) {
        Dipendente found = this.findById(dipendenteId);
        this.dipendentiRepository.delete(found);
    }

    public Dipendente findByEmail(String email) {
        return this.dipendentiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }

    public Dipendente uploadImage(UUID dipendenteId, MultipartFile file) throws IOException {
        Dipendente found = this.findById(dipendenteId);
        String url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(url);
        return this.dipendentiRepository.save(found);
    }
}
