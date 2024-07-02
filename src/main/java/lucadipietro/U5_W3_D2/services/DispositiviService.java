package lucadipietro.U5_W3_D2.services;

import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.entities.Dispositivo;
import lucadipietro.U5_W3_D2.enums.StatoDispositivo;
import lucadipietro.U5_W3_D2.enums.TipoDispositivo;
import lucadipietro.U5_W3_D2.exceptions.BadRequestException;
import lucadipietro.U5_W3_D2.exceptions.NotFoundException;
import lucadipietro.U5_W3_D2.payloads.DispositiviDTO;
import lucadipietro.U5_W3_D2.repositories.DispositiviRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DispositiviService {
    @Autowired
    private DispositiviRepository dispositiviRepository;

    @Autowired
    private DipendentiService dipendentiService;

    public Page<Dispositivo> getDispositivi(int pageNumber, int pageSize, String sortBy){
        if(pageSize > 100) pageSize = 100;
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        return dispositiviRepository.findAll(pageable);
    }

    public Dispositivo save(DispositiviDTO body){
        Dispositivo newDispositivo = new Dispositivo(convertToStringTipo(body.tipo()),convertToStringStato(body.stato()));
        return this.dispositiviRepository.save(newDispositivo);
    }

    public Dispositivo findById(UUID dispositivoId) {
        return this.dispositiviRepository.findById(dispositivoId).orElseThrow(() -> new NotFoundException(dispositivoId));
    }

    public Dispositivo findByIdAndUpdate(UUID dispositivoId, DispositiviDTO body){
        Dispositivo found = this.findById(dispositivoId);
        found.setTipo(convertToStringTipo(body.tipo()));
        found.setStato(convertToStringStato(body.stato()));
        return this.dispositiviRepository.save(found);
    }

    public Dispositivo assegna(UUID dispositivoId, UUID dipendenteId){
        Dispositivo foundDispositivo = this.findById(dispositivoId);
        Dipendente foundDipendente = this.dipendentiService.findById(dipendenteId);
        if(foundDispositivo.getStato() == StatoDispositivo.DISMESSO){
            throw new BadRequestException("Il dispositivo è dismesso");
        }
        else if (foundDispositivo.getStato() == StatoDispositivo.IN_MANUTENZIONE){
            throw new BadRequestException("Il dispositivo è in manutenzione");
        }
        else if (foundDispositivo.getStato() == StatoDispositivo.ASSEGNATO){
            throw new BadRequestException("Il dispositivo è gia assegnato");
        }
        else {
            foundDispositivo.setStato(StatoDispositivo.ASSEGNATO);
            foundDispositivo.setDipendente(foundDipendente);
            return this.dispositiviRepository.save(foundDispositivo);
        }
    }

    public void findByIdAndDelete(UUID dispositivoId) {
        Dispositivo found = this.findById(dispositivoId);
        this.dispositiviRepository.delete(found);
    }

    public static TipoDispositivo convertToStringTipo(String tipo){
        try {
            return TipoDispositivo.valueOf(tipo.toUpperCase());
        }catch (IllegalArgumentException exception){
            throw new BadRequestException("Il tipo del dispositivo inserito non è corretto, devi inserire SMARTPHONE,TABLET O LAPTOP!");
        }
    }

    public static StatoDispositivo convertToStringStato(String stato){
        try {
            return StatoDispositivo.valueOf(stato.toUpperCase());
        }catch (IllegalArgumentException exception){
            throw new BadRequestException("Lo stato del dispositivo inserito non è corretto, devi inserire DISPONIBILE,ASSEGNATO,IN_MANUTENZIONE o DISMESSO");
        }
    }
}
