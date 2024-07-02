package lucadipietro.U5_W3_D2.controllers;

import lucadipietro.U5_W3_D2.entities.Dispositivo;
import lucadipietro.U5_W3_D2.exceptions.BadRequestException;
import lucadipietro.U5_W3_D2.payloads.DispositiviDTO;
import lucadipietro.U5_W3_D2.services.DispositiviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/dispositivi")
public class DispositiviController {
    @Autowired
    private DispositiviService dispositiviService;

    @GetMapping
    public Page<Dispositivo> getAllDispositivi(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy){
        return this.dispositiviService.getDispositivi(page,size,sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dispositivo saveDispositivo(@RequestBody @Validated DispositiviDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new BadRequestException(validationResult.getAllErrors());
        } else {
            return this.dispositiviService.save(body);
        }
    }

    @GetMapping("/{dispositivoId}")
    public Dispositivo findById(@PathVariable UUID dispositivoId){
        return this.dispositiviService.findById(dispositivoId);
    }

    @PutMapping("/{dispositivoId}")
    public Dispositivo findByIdAndUpdate(@PathVariable UUID dispositivoId, @RequestBody @Validated DispositiviDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new BadRequestException(validationResult.getAllErrors());
        }else {
            return this.dispositiviService.findByIdAndUpdate(dispositivoId,body);
        }
    }

    @DeleteMapping("/{dispositivoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID dispositivoId){
        this.dispositiviService.findByIdAndDelete(dispositivoId);
    }

    @PatchMapping("/{dispositivoId}/assegna")
    public Dispositivo assegnaDispositivo(@PathVariable UUID dispositivoId,@RequestParam("dipendenteId") UUID dipendenteId){
        return this.dispositiviService.assegna(dispositivoId,dipendenteId);
    }
}
