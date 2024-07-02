package lucadipietro.U5_W3_D2.controllers;

import lucadipietro.U5_W3_D2.entities.Dipendente;
import lucadipietro.U5_W3_D2.exceptions.BadRequestException;
import lucadipietro.U5_W3_D2.payloads.DipendentiDTO;
import lucadipietro.U5_W3_D2.payloads.RuoloDTO;
import lucadipietro.U5_W3_D2.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente){
        return currentAuthenticatedDipendente;
    }

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente, @RequestBody DipendentiDTO body){
        return this.dipendentiService.findByIdAndUpdate(currentAuthenticatedDipendente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente){
        this.dipendentiService.findByIdAndDelete(currentAuthenticatedDipendente.getId());
    }

    @PatchMapping("/me/avatar")
    public Dipendente updateAvatar(@PathVariable UUID dipendenteId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.dipendentiService.uploadImage(dipendenteId, image);
    }

    @GetMapping("/{dipendenteId}")
    public Dipendente findById(@PathVariable UUID dipendenteId){
        return this.dipendentiService.findById(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('CAPO_REPARTO')")
    public Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId,@RequestBody @Validated DipendentiDTO body,BindingResult validationResult){
        if(validationResult.hasErrors()){
            throw new BadRequestException(validationResult.getAllErrors());
        }else {
            return this.dipendentiService.findByIdAndUpdate(dipendenteId,body);
        }
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('CAPO_REPARTO')")
    public void findByIdAndDelete(@PathVariable UUID dipendenteId){
        this.dipendentiService.findByIdAndDelete(dipendenteId);
    }

    @PatchMapping("/{dipendenteId}/role")
    @PreAuthorize("hasAuthority('CAPO_REPARTO')")
    public Dipendente updateRole(@PathVariable UUID id, @RequestBody RuoloDTO ruolo) {
        return dipendentiService.updateRole(id, ruolo);
    }

    @PatchMapping("/{dipendenteId}/avatar")
    @PreAuthorize("hasAuthority('CAPO_REPARTO')")
    public Dipendente uploadAvatar(@PathVariable UUID dipendenteId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.dipendentiService.uploadImage(dipendenteId, image);
    }
}
