package lucadipietro.U5_W3_D2.controllers;

import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.exceptions.BadRequestException;
import lucadipietro.U5_W3_D2.payloads.DipendentiDTO;
import lucadipietro.U5_W3_D2.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {
    @Autowired
    private DipendentiService dipendentiService;

    @GetMapping
    public Page<Dipendente> getAllDipendenti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy){
        return this.dipendentiService.getDipendenti(page,size,sortBy);
    }

    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable UUID dipendenteId){
        return this.dipendentiService.findById(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    public Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId,@RequestBody @Validated DipendentiDTO body,BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new BadRequestException(validationResult.getAllErrors());
        }else {
            return this.dipendentiService.findByIdAndUpdate(dipendenteId,body);
        }
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID dipendenteId){
        this.dipendentiService.findByIdAndDelete(dipendenteId);
    }

    @PatchMapping("/{dipendenteId}/avatar")
    public Dipendente uploadAvatar(@PathVariable UUID dipendenteId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.dipendentiService.uploadImage(dipendenteId, image);
    }
}
