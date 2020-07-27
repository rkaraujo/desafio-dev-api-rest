package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import renato.araujo.account.manager.dto.MessageDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new MessageDTO(errors);
    }

    @ExceptionHandler(AbstractBusinessException.class)
    public ResponseEntity<MessageDTO> handleValidationExceptions(AbstractBusinessException ex) {
        return new ResponseEntity<>(new MessageDTO(Collections.singletonList(ex.getMessage())), ex.getCode());
    }

}
