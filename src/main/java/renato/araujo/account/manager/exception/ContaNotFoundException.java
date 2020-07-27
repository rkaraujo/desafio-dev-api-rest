package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;

public class ContaNotFoundException extends AbstractBusinessException {

    public ContaNotFoundException() {
        super("Conta nao encontrada", HttpStatus.NOT_FOUND);
    }

}
