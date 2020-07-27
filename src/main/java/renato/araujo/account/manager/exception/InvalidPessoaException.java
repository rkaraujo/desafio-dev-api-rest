package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;

public class InvalidPessoaException extends AbstractBusinessException {

    public InvalidPessoaException() {
        super("Pessoa invalida", HttpStatus.BAD_REQUEST);
    }

}
