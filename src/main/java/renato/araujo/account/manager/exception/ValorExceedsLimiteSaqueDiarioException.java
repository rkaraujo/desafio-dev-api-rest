package renato.araujo.account.manager.exception;

import org.springframework.http.HttpStatus;

public class ValorExceedsLimiteSaqueDiarioException extends AbstractBusinessException {

    public ValorExceedsLimiteSaqueDiarioException() {
        super("Valor do saque excede o limite de saque diario", HttpStatus.BAD_REQUEST);
    }
}
