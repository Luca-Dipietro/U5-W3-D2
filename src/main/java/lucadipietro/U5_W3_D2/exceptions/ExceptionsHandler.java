package lucadipietro.U5_W3_D2.exceptions;

import lucadipietro.U5_W3_D2.payloads.ErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequest(BadRequestException ex){
        if (ex.getErrorsList() != null) {
            String message = ex.getErrorsList().stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.joining(". "));
            return new ErrorsDTO(message, LocalDateTime.now());

        } else {
            return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(NotFoundException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleGenericError(Exception ex){
        ex.printStackTrace();
        return new ErrorsDTO("Errore lato server, verificheremo subito!",LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsDTO handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorsDTO("Non puoi usufruire di queste funzionalità", LocalDateTime.now());
    }
}
