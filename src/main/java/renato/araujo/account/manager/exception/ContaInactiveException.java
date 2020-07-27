package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;

public class ContaInactiveException extends AbstractBusinessException {

    public ContaInactiveException() {
        super("Conta inativa", HttpStatus.BAD_REQUEST);
    }
}
